/*
 * Copyright (c) 2018, 2025, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
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
 * @test
 * @bug 8181157 8202537 8234347 8236548 8261279 8322647 8174269 8346948
 * @modules jdk.localedata
 * @summary Checks CLDR time zone names are generated correctly at
 * either build or runtime
 * @run testng TimeZoneNamesTest
 */

import java.text.DateFormatSymbols;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class TimeZoneNamesTest {

    @DataProvider
    Object[][] sampleTZs() {
        return new Object[][] {
            // tzid, locale, style, expected

            // This list is as of CLDR version 47, and should be examined
            // on the CLDR data upgrade.

            // no "metazone" zones
            {"Asia/Srednekolymsk",      Locale.US, "Srednekolymsk Standard Time",
                                                    "GMT+11:00",
                                                    "Srednekolymsk Daylight Time",
                                                    "GMT+12:00",
                                                    "Srednekolymsk Time",
                                                    "GMT+11:00"},
            {"Asia/Srednekolymsk",      Locale.FRANCE, "Srednekolymsk (heure standard)",
                                                    "UTC+11:00",
                                                    "Srednekolymsk (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+12:00",
                                                    "heure : Srednekolymsk",
                                                    "UTC+11:00"},
            {"America/Punta_Arenas",    Locale.US, "Punta Arenas Standard Time",
                                                    "GMT-03:00",
                                                    "Punta Arenas Daylight Time",
                                                    "GMT-02:00",
                                                    "Punta Arenas Time",
                                                    "GMT-03:00"},
            {"America/Punta_Arenas",    Locale.FRANCE, "Punta Arenas (heure standard)",
                                                    "UTC\u221203:00",
                                                    "Punta Arenas (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC\u221202:00",
                                                    "heure : Punta Arenas",
                                                    "UTC\u221203:00"},
            {"Asia/Famagusta",          Locale.US, "Famagusta Standard Time",
                                                    "EET",
                                                    "Famagusta Daylight Time",
                                                    "EEST",
                                                    "Famagusta Time",
                                                    "EET"},
            {"Asia/Famagusta",          Locale.FRANCE, "Famagouste (heure standard)",
                                                    "EET",
                                                    "Famagouste (heure d\u2019\u00e9t\u00e9)",
                                                    "EEST",
                                                    "heure : Famagouste",
                                                    "EET"},
            {"Europe/Astrakhan",        Locale.US, "Astrakhan Standard Time",
                                                    "GMT+04:00",
                                                    "Astrakhan Daylight Time",
                                                    "GMT+05:00",
                                                    "Astrakhan Time",
                                                    "GMT+04:00"},
            {"Europe/Astrakhan",        Locale.FRANCE, "Astrakhan (heure standard)",
                                                    "UTC+04:00",
                                                    "Astrakhan (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+05:00",
                                                    "heure : Astrakhan",
                                                    "UTC+04:00"},
            {"Europe/Saratov",          Locale.US, "Saratov Standard Time",
                                                    "GMT+04:00",
                                                    "Saratov Daylight Time",
                                                    "GMT+05:00",
                                                    "Saratov Time",
                                                    "GMT+04:00"},
            {"Europe/Saratov",          Locale.FRANCE, "Saratov (heure standard)",
                                                    "UTC+04:00",
                                                    "Saratov (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+05:00",
                                                    "heure : Saratov",
                                                    "UTC+04:00"},
            {"Europe/Ulyanovsk",        Locale.US, "Ulyanovsk Standard Time",
                                                    "GMT+04:00",
                                                    "Ulyanovsk Daylight Time",
                                                    "GMT+05:00",
                                                    "Ulyanovsk Time",
                                                    "GMT+04:00"},
            {"Europe/Ulyanovsk",        Locale.FRANCE, "Oulianovsk (heure standard)",
                                                    "UTC+04:00",
                                                    "Oulianovsk (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+05:00",
                                                    "heure : Oulianovsk",
                                                    "UTC+04:00"},
            {"Pacific/Bougainville",    Locale.US, "Bougainville Standard Time",
                                                    "GMT+11:00",
                                                    "Bougainville Daylight Time",
                                                    "GMT+11:00",
                                                    "Bougainville Time",
                                                    "GMT+11:00"},
            {"Pacific/Bougainville",    Locale.FRANCE, "Bougainville (heure standard)",
                                                    "UTC+11:00",
                                                    "Bougainville (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+11:00",
                                                    "heure : Bougainville",
                                                    "UTC+11:00"},
            {"Europe/Istanbul",    Locale.US, "Istanbul Standard Time",
                                                    "GMT+03:00",
                                                    "Istanbul Daylight Time",
                                                    "GMT+04:00",
                                                    "Istanbul Time",
                                                    "GMT+03:00"},
            {"Europe/Istanbul",    Locale.FRANCE, "Istanbul (heure standard)",
                                                    "UTC+03:00",
                                                    "Istanbul (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+04:00",
                                                    "heure : Istanbul",
                                                    "UTC+03:00"},
            {"Asia/Istanbul",    Locale.US, "Istanbul Standard Time",
                                                    "GMT+03:00",
                                                    "Istanbul Daylight Time",
                                                    "GMT+04:00",
                                                    "Istanbul Time",
                                                    "GMT+03:00"},
            {"Asia/Istanbul",    Locale.FRANCE, "Istanbul (heure standard)",
                                                    "UTC+03:00",
                                                    "Istanbul (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+04:00",
                                                    "heure : Istanbul",
                                                    "UTC+03:00"},
            {"Turkey",    Locale.US, "Istanbul Standard Time",
                                                    "GMT+03:00",
                                                    "Istanbul Daylight Time",
                                                    "GMT+04:00",
                                                    "Istanbul Time",
                                                    "GMT+03:00"},
            {"Turkey",    Locale.FRANCE, "Istanbul (heure standard)",
                                                    "UTC+03:00",
                                                    "Istanbul (heure d\u2019\u00e9t\u00e9)",
                                                    "UTC+04:00",
                                                    "heure : Istanbul",
                                                    "UTC+03:00"},

            // Short names derived from TZDB at build time
            {"Europe/Lisbon",    Locale.US, "Western European Standard Time",
                        "WET",
                        "Western European Summer Time",
                        "WEST",
                        "Western European Time",
                        "WET"},
            {"Atlantic/Azores",    Locale.US, "Azores Standard Time",
                        "GMT-01:00",
                        "Azores Summer Time",
                        "GMT",
                        "Azores Time",
                        "GMT-01:00"},
            {"Australia/Perth",    Locale.US, "Australian Western Standard Time",
                        "AWST",
                        "Australian Western Daylight Time",
                        "AWDT",
                        "Australian Western Time",
                        "AWT"},
            {"Africa/Harare",    Locale.US, "Central Africa Time",
                        "CAT",
                        "Harare Daylight Time",
                        "CAT",
                        "Harare Time",
                        "CAT"},
            {"Europe/Dublin",    Locale.US, "Greenwich Mean Time",
                        "GMT",
                        "Irish Standard Time",
                        "IST",
                        "Dublin Time",
                        "GMT"},
            {"Pacific/Gambier",    Locale.US, "Gambier Time",
                        "GMT-09:00",
                        "Gambier Daylight Time",
                        "GMT-09:00",
                        "Gambier Time",
                        "GMT-09:00"},
        };
    }


    @Test(dataProvider="sampleTZs")
    public void test_tzNames(String tzid, Locale locale, String lstd, String sstd, String ldst, String sdst, String lgen, String sgen) {
        // Standard time
        assertEquals(TimeZone.getTimeZone(tzid).getDisplayName(false, TimeZone.LONG, locale), lstd);
        assertEquals(TimeZone.getTimeZone(tzid).getDisplayName(false, TimeZone.SHORT, locale), sstd);

        // daylight saving time
        assertEquals(TimeZone.getTimeZone(tzid).getDisplayName(true, TimeZone.LONG, locale), ldst);
        assertEquals(TimeZone.getTimeZone(tzid).getDisplayName(true, TimeZone.SHORT, locale), sdst);

        // generic name
        assertEquals(ZoneId.of(tzid).getDisplayName(TextStyle.FULL, locale), lgen);
        assertEquals(ZoneId.of(tzid).getDisplayName(TextStyle.SHORT, locale), sgen);
    }

    // Make sure getZoneStrings() returns non-empty string array
    @Test
    public void test_getZoneStrings() {
        assertFalse(
            Locale.availableLocales()
                .limit(30)
                .peek(l -> System.out.println("Locale: " + l))
                .map(l -> DateFormatSymbols.getInstance(l).getZoneStrings())
                .flatMap(Arrays::stream)
                .peek(names -> System.out.println("    tz: " + names[0]))
                .flatMap(Arrays::stream)
                .anyMatch(name -> Objects.isNull(name) || name.isEmpty()),
            "getZoneStrings() returned array containing non-empty string element(s)");
    }
}
