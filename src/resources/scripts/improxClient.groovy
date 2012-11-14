//
// usage: groovy improx-client.groovy <COMMAND_LINE>
//        groovyclient improx-client.groovy <COMMAND_LINE>  (RECOMMENDED)
//
//  e.g.
//    grooovy improx-client.groovy help
//    grooovy improx-client.groovy test-app unit: TheDomainTest
//

//---------------------------------------
// Definition
//

class InteractiveModeProxyClient {

    private static final int DEFAULT_PORT = 8081
    private Socket socket

    def invoke(String command) {
        validate(command)
        connect(port)
        send(command)
        waitForResult()
    }

    private validate(String command) {
        if (command.trim().empty) {
            System.err.println "ERROR: Command not found."
            System.exit 1
        }
    }

    private getPort() {
        (System.getProperty("improx.port") ?: DEFAULT_PORT) as int
    }

    private connect(int port) {
        try {
            socket = new Socket("localhost", port)
        } catch (ConnectException e) {
            System.err.println "ERROR: Failed to connect to server via port $port."
            System.err.println " Install improx plugin into your project and invoke improx-start before connecting."
            System.exit 1
        }
    }

    private send(String command) {
        socket << command << "\n"
    }

    private waitForResult() {
        socket.withStreams { ins, out ->
            while (true) {
                def (text, eof) = readLine(ins)
                println text
                if (eof) {
                    System.exit 0
                }
            }
        }
    }

    private readLine(InputStream ins) {
        def out = new ByteArrayOutputStream()
        int ch
        while ((ch = ins.read()) != -1) {
            if (ch == '\n') { // LF (fixed)
                return [out.toString(), false] // 2nd arg is EOF flag
            }
            out.write((byte) ch)
        }
        return [out.toString(), true] // 2nd arg is EOF flag
    }
}

//---------------------------------------
// Main
//

String command = args.join(' ')
new InteractiveModeProxyClient().invoke(command)
