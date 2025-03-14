/*
 * Copyright (c) 2000, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 *
 *  (C) Copyright IBM Corp. 1999 All Rights Reserved.
 *  Copyright 1997 The Open Group Research Institute.  All rights reserved.
 */

package sun.security.krb5;

import java.security.Security;
import java.util.Locale;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.NetClient;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import sun.security.krb5.internal.KRBError;

import static sun.security.krb5.internal.Krb5.DEBUG;

/**
 * KDC-REQ/KDC-REP communication. No more base class for KrbAsReq and
 * KrbTgsReq. This class is now communication only.
 */
public final class KdcComm {

    // The following settings can be configured in [libdefaults]
    // section of krb5.conf, which are global for all realms. Each of
    // them can also be defined in a realm, which overrides value here.

    /**
     * max retry time for a single KDC, default Krb5.KDC_RETRY_LIMIT (3),
     * Must be > 0.
     */
    private static int defaultKdcRetryLimit;
    /**
     * timeout requesting a ticket from KDC, in millisec, default
     * Krb5.KDC_TIMEOUT (30000). Must be > 0.
     */
    private static int defaultKdcTimeout;
    /**
     * max UDP packet size, default Krb5.KDC_DEFAULT_UDP_PREF_LIMIT (1465).
     * Must be >= 0 and <= Krb5.KDC_HARD_UDP_LIMIT (32700).
     */
    private static int defaultUdpPrefLimit;

    /**
     * What to do when a KDC is unavailable, specified in the
     * java.security file with key krb5.kdc.bad.policy.
     * Possible values can be TRY_LAST or TRY_LESS. Reloaded when refreshed.
     */
    private enum BpType {
        NONE, TRY_LAST, TRY_LESS
    }
    private static int tryLessMaxRetries = 1;
    private static int tryLessTimeout = 5000;

    private static BpType badPolicy;

    static {
        initStatic();
    }

    /**
     * Read global settings
     */
    public static void initStatic() {
        String value = Security.getProperty("krb5.kdc.bad.policy");
        if (value != null) {
            value = value.toLowerCase(Locale.ENGLISH);
            String[] ss = value.split(":");
            if ("tryless".equals(ss[0])) {
                if (ss.length > 1) {
                    String[] params = ss[1].split(",");
                    try {
                        int tmp0 = Integer.parseInt(params[0]);
                        if (params.length > 1) {
                            tryLessTimeout = Integer.parseInt(params[1]);
                        }
                        // Assign here in case of exception at params[1]
                        tryLessMaxRetries = tmp0;
                    } catch (NumberFormatException nfe) {
                        // Ignored. Please note that tryLess is recognized and
                        // used, parameters using default values
                        if (DEBUG != null) {
                            DEBUG.println("Invalid krb5.kdc.bad.policy" +
                                    " parameter for tryLess: " +
                                    value + ", use default");
                        }
                    }
                }
                badPolicy = BpType.TRY_LESS;
            } else if ("trylast".equals(ss[0])) {
                badPolicy = BpType.TRY_LAST;
            } else {
                badPolicy = BpType.NONE;
            }
        } else {
            badPolicy = BpType.NONE;
        }


        int timeout = -1;
        int max_retries = -1;
        int udp_pref_limit = -1;

        try {
            Config cfg = Config.getInstance();
            String temp = cfg.get("libdefaults", "kdc_timeout");
            timeout = parseTimeString(temp);

            temp = cfg.get("libdefaults", "max_retries");
            max_retries = parseNonNegativeIntString(temp);
            temp = cfg.get("libdefaults", "udp_preference_limit");
            udp_pref_limit = parseNonNegativeIntString(temp);
        } catch (Exception exc) {
           // ignore any exceptions; use default values
           if (DEBUG != null) {
                DEBUG.println ("Exception in getting KDC communication " +
                                    "settings, using default value " +
                                    exc.getMessage());
           }
        }
        defaultKdcTimeout = timeout > 0 ? timeout : Krb5.KDC_TIMEOUT;
        defaultKdcRetryLimit =
                max_retries > 0 ? max_retries : Krb5.KDC_RETRY_LIMIT;

        if (udp_pref_limit < 0) {
            defaultUdpPrefLimit = Krb5.KDC_DEFAULT_UDP_PREF_LIMIT;
        } else if (udp_pref_limit > Krb5.KDC_HARD_UDP_LIMIT) {
            defaultUdpPrefLimit = Krb5.KDC_HARD_UDP_LIMIT;
        } else {
            defaultUdpPrefLimit = udp_pref_limit;
        }

        KdcAccessibility.reset();
    }

    /**
     * The instance fields
     */
    private final String realm;

    public KdcComm(String realm) throws KrbException {
        if (realm == null) {
            realm = Config.getInstance().getDefaultRealm();
            if (realm == null) {
                throw new KrbException(Krb5.KRB_ERR_GENERIC,
                                       "Cannot find default realm");
            }
        }
        this.realm = realm;
    }

    public byte[] send(KrbKdcReq req)
        throws IOException, KrbException {
        int udpPrefLimit = getRealmSpecificValue(
                realm, "udp_preference_limit", defaultUdpPrefLimit, false);

        byte[] obuf = req.encoding();
        boolean useTCP = obuf != null && obuf.length > udpPrefLimit;

        return send(req, useTCP);
    }

    private byte[] send(KrbKdcReq req, boolean useTCP)
        throws IOException, KrbException {

        if (req == null)
            return null;
        Config cfg = Config.getInstance();

        String kdcList = cfg.getKDCList(realm);
        if (kdcList == null) {
            throw new KrbException("Cannot get kdc for realm " + realm);
        }
        // tempKdc may include the port number also
        Iterator<String> tempKdc = KdcAccessibility.list(kdcList).iterator();
        if (!tempKdc.hasNext()) {
            throw new KrbException("Cannot get kdc for realm " + realm);
        }
        byte[] ibuf = null;
        try {
            ibuf = sendIfPossible(req, tempKdc.next(), useTCP);
        } catch(Exception first) {
            boolean ok = false;
            while(tempKdc.hasNext()) {
                try {
                    ibuf = sendIfPossible(req, tempKdc.next(), useTCP);
                    ok = true;
                    break;
                } catch(Exception ignore) {}
            }
            if (!ok) throw first;
        }
        if (ibuf == null) {
            throw new IOException("Cannot get a KDC reply");
        }
        return ibuf;
    }

    // send the KDC Request to the specified KDC
    // failover to using TCP if useTCP is not set and response is too big
    private byte[] sendIfPossible(KrbKdcReq req, String tempKdc, boolean useTCP)
        throws IOException, KrbException {

        try {
            byte[] ibuf = send(req, tempKdc, useTCP);
            KRBError ke = null;
            try {
                ke = new KRBError(ibuf);
            } catch (Exception e) {
                // OK
            }
            if (ke != null) {
                if (ke.getErrorCode() ==
                    Krb5.KRB_ERR_RESPONSE_TOO_BIG) {
                    ibuf = send(req, tempKdc, true);
                } else if (ke.getErrorCode() ==
                        Krb5.KDC_ERR_SVC_UNAVAILABLE) {
                    throw new KrbException("A service is not available");
                } else if (ke.getErrorCode() == Krb5.KDC_ERR_BADOPTION
                        && Credentials.S4U2PROXY_ACCEPT_NON_FORWARDABLE
                        && req instanceof KrbTgsReq tgsReq) {
                    Credentials extra = tgsReq.getAdditionalCreds();
                    if (extra != null && !extra.isForwardable()) {
                        throw new KrbException("S4U2Proxy with non-forwardable ticket");
                    }
                }
            }
            KdcAccessibility.removeBad(tempKdc);
            return ibuf;
        } catch(Exception e) {
            if (DEBUG != null) {
                DEBUG.println(">>> KrbKdcReq send: error trying " +
                        tempKdc);
                e.printStackTrace(DEBUG.getPrintStream());
            }
            KdcAccessibility.addBad(tempKdc);
            throw e;
        }
    }

    // send the KDC Request to the specified KDC

    private byte[] send(KrbKdcReq req, String tempKdc, boolean useTCP)
        throws IOException, KrbException {

        if (req == null)
            return null;

        int port = Krb5.KDC_INET_DEFAULT_PORT;
        int retries = getRealmSpecificValue(
                realm, "max_retries", defaultKdcRetryLimit, true);
        int timeout = getRealmSpecificValue(
                realm, "kdc_timeout", defaultKdcTimeout, true);
        if (badPolicy == BpType.TRY_LESS &&
                KdcAccessibility.isBad(tempKdc)) {
            if (retries > tryLessMaxRetries) {
                retries = tryLessMaxRetries; // less retries
            }
            if (timeout > tryLessTimeout) {
                timeout = tryLessTimeout; // less time
            }
        }

        String kdc = null;
        String portStr = null;

        if (tempKdc.charAt(0) == '[') {     // Explicit IPv6 in []
            int pos = tempKdc.indexOf(']', 1);
            if (pos == -1) {
                throw new IOException("Illegal KDC: " + tempKdc);
            }
            kdc = tempKdc.substring(1, pos);
            if (pos != tempKdc.length() - 1) {  // with port number
                if (tempKdc.charAt(pos+1) != ':') {
                    throw new IOException("Illegal KDC: " + tempKdc);
                }
                portStr = tempKdc.substring(pos+2);
            }
        } else {
            int colon = tempKdc.indexOf(':');
            if (colon == -1) {      // Hostname or IPv4 host only
                kdc = tempKdc;
            } else {
                int nextColon = tempKdc.indexOf(':', colon+1);
                if (nextColon > 0) {    // >=2 ":", IPv6 with no port
                    kdc = tempKdc;
                } else {                // 1 ":", hostname or IPv4 with port
                    kdc = tempKdc.substring(0, colon);
                    portStr = tempKdc.substring(colon+1);
                }
            }
        }
        if (portStr != null) {
            int tempPort = parseNonNegativeIntString(portStr);
            if (tempPort > 0)
                port = tempPort;
        }

        byte[] obuf = req.encoding();
        if (DEBUG != null) {
            DEBUG.println(">>> KrbKdcReq send: kdc=" + kdc
                               + (useTCP ? " TCP:":" UDP:")
                               +  port +  ", timeout="
                               + timeout
                               + ", number of retries ="
                               + retries
                               + ", #bytes=" + obuf.length);
        }

        byte[] ibuf = null;

        for (int i=1; i <= retries; i++) {
            String proto = useTCP?"TCP":"UDP";
            if (DEBUG != null) {
                DEBUG.println(">>> KDCCommunication: kdc=" + kdc
                        + " " + proto + ":"
                        + port + ", timeout="
                        + timeout
                        + ",Attempt =" + i
                        + ", #bytes=" + obuf.length);
            }
            try (NetClient kdcClient = NetClient.getInstance(
                    proto, kdc, port, timeout)) {
                kdcClient.send(obuf);
                ibuf = kdcClient.receive();
                break;
            } catch (SocketTimeoutException se) {
                if (DEBUG != null) {
                    DEBUG.println ("SocketTimeOutException with " +
                            "attempt: " + i);
                }
                if (i == retries) {
                    ibuf = null;
                    throw se;
                }
            }
        }
        if (DEBUG != null) {
            DEBUG.println(">>> KrbKdcReq send: #bytes read="
                    + (ibuf != null ? ibuf.length : 0));
        }
        return ibuf;
    }

    /**
     * Parses a time value string. If it ends with "s", parses as seconds.
     * Otherwise, parses as milliseconds.
     * @param s the time string
     * @return the integer value in milliseconds, or -1 if input is null or
     * has an invalid format
     */
    private static int parseTimeString(String s) {
        if (s == null) {
            return -1;
        }
        if (s.endsWith("s")) {
            int seconds = parseNonNegativeIntString(s.substring(0, s.length()-1));
            return (seconds < 0) ? -1 : (seconds*1000);
        } else {
            return parseNonNegativeIntString(s);
        }
    }

    /**
     * Returns krb5.conf setting of {@code key} for a specific realm,
     * which can be:
     * 1. defined in the sub-stanza for the given realm inside [realms], or
     * 2. defined in [libdefaults], or
     * 3. defValue
     * @param realm the given realm in which the setting is requested. Returns
     * the global setting if null
     * @param key the key for the setting
     * @param defValue default value
     * @param mustBePositive true if value must be >0, false if value must be >=0
     * @return a value for the key
     */
    private int getRealmSpecificValue(String realm, String key, int defValue,
            boolean mustBePositive) {
        int v = defValue;

        if (realm == null) return v;

        int temp = -1;
        try {
            String value =
               Config.getInstance().get("realms", realm, key);
            if (key.equals("kdc_timeout")) {
                temp = parseTimeString(value);
            } else {
                temp = parseNonNegativeIntString(value);
            }
        } catch (Exception exc) {
            // Ignored, defValue will be picked up
        }

        if (mustBePositive) {
            if (temp > 0) v = temp;
        } else {
            if (temp >= 0) v = temp;
        }

        return v;
    }

    private static int parseNonNegativeIntString(String intString) {
        if (intString == null)
            return -1;

        int ret = -1;

        try {
            ret = Integer.parseInt(intString);
        } catch (Exception exc) {
            return -1;
        }

        if (ret >= 0)
            return ret;

        return -1;
    }

    /**
     * Maintains a KDC accessible list. Unavailable KDCs are put into a
     * secondary KDC list. When a KDC in the secondary list is available,
     * it is removed from there. No insertion order in the secondary KDC list.
     *
     * There are two methods to deal with KDCs in the secondary KDC list.
     * 1. Only try them when they are the only known KDCs.
     * 2. Still try them, but with fewer retries and a smaller timeout value.
     */
    static class KdcAccessibility {
        // Known bad KDCs
        private static Set<String> bads = new HashSet<>();

        private static synchronized void addBad(String kdc) {
            if (DEBUG != null) {
                DEBUG.println(">>> KdcAccessibility: add " + kdc);
            }
            bads.add(kdc);
        }

        private static synchronized void removeBad(String kdc) {
            if (DEBUG != null) {
                DEBUG.println(">>> KdcAccessibility: remove " + kdc);
            }
            bads.remove(kdc);
        }

        private static synchronized boolean isBad(String kdc) {
            return bads.contains(kdc);
        }

        private static synchronized void reset() {
            if (DEBUG != null) {
                DEBUG.println(">>> KdcAccessibility: reset");
            }
            bads.clear();
        }

        // Returns a preferred KDC list by putting the bad ones at the end
        private static synchronized List<String> list(String kdcList) {
            StringTokenizer st = new StringTokenizer(kdcList);
            List<String> list = new ArrayList<>();
            if (badPolicy == BpType.TRY_LAST) {
                List<String> badkdcs = new ArrayList<>();
                while (st.hasMoreTokens()) {
                    String t = st.nextToken();
                    if (bads.contains(t)) badkdcs.add(t);
                    else list.add(t);
                }
                // Bad KDCs are put at last
                list.addAll(badkdcs);
            } else {
                // All KDCs are returned in their original order,
                // This include TRY_LESS and NONE
                while (st.hasMoreTokens()) {
                    list.add(st.nextToken());
                }
            }
            return list;
        }
    }
}

