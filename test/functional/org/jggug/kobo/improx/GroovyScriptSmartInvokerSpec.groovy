/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jggug.kobo.improx

class GroovyScriptSmartInvokerSpec extends AbstractSmartInvokerSpec {

    @Override
    String invokeFile(file, List env) {
        // multiple invocation of external process causes too complex to handle.
        // so here echo back of script by debug mode only for testing is used.
        def script = System.properties["user.dir"] + "/build/improx-resources/scripts/improxSmartInvoker.groovy"
        def process = ["groovy", script, file].execute(env, null)
        def errText = process.err.text
        assert errText == "" || hasOnlyPickedUpLines(errText)
        return process.in.text
    }

    private static boolean hasOnlyPickedUpLines(String errText) {
        errText.readLines().every { line -> line.startsWith("Picked up _JAVA_OPTIONS:") }
    }
}
