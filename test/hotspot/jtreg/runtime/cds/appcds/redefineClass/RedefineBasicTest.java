/*
 * Copyright (c) 2016, 2024, Oracle and/or its affiliates. All rights reserved.
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
 *
 */

/*
 * @test
 * @summary Run /serviceability/jvmti/RedefineClasses/RedefineRunningMethods in AppCDS mode to
 *          make sure class redefinition works with CDS.
 * @requires vm.cds
 * @requires vm.jvmti
 * @library /test/lib /test/hotspot/jtreg/serviceability/jvmti/RedefineClasses /test/hotspot/jtreg/runtime/cds/appcds
 * @run driver RedefineClassHelper
 * @build jdk.test.whitebox.WhiteBox RedefineBasic
 * @run driver RedefineBasicTest
 */

import jdk.test.lib.process.OutputAnalyzer;
import jdk.test.lib.helpers.ClassFileInstaller;

public class RedefineBasicTest {
    public static String sharedClasses[] = {
        "RedefineBasic",
        "RedefineBasic_B",
        "RedefineBasic$SubclassOfB",
        "RedefineBasic$Subclass2OfB",
        "RedefineClassHelper",
        "jdk/test/lib/compiler/InMemoryJavaCompiler",
        "jdk/test/lib/compiler/InMemoryJavaCompiler$FileManagerWrapper",
        "jdk/test/lib/compiler/InMemoryJavaCompiler$FileManagerWrapper$1",
        "jdk/test/lib/compiler/InMemoryJavaCompiler$SourceFile",
        "jdk/test/lib/compiler/InMemoryJavaCompiler$ClassFile"
    };

    public static void main(String[] args) throws Exception {
        String wbJar =
            ClassFileInstaller.writeJar("WhiteBox.jar", "jdk.test.whitebox.WhiteBox");
        String appJar =
            ClassFileInstaller.writeJar("RedefineBasic.jar", sharedClasses);
        String useWb = "-Xbootclasspath/a:" + wbJar;

        OutputAnalyzer output;
        TestCommon.testDump(appJar, sharedClasses, useWb);

        // redefineagent.jar is created by executing "@run driver RedefineClassHelper"
        // which should be called before executing RedefineBasicTest
        output = TestCommon.exec(appJar, useWb,
                                 "-XX:+UnlockDiagnosticVMOptions",
                                 "-XX:+WhiteBoxAPI",
                                 "-javaagent:redefineagent.jar",
                                 "RedefineBasic");
        TestCommon.checkExec(output);
    }
}
