package student;

import student.controller.ArgsController;

/** Program entry point. */
public final class DNInfoApp {

    private DNInfoApp() {
        // empty
    }

    /**
     * Runs the command-line application.
     *
     * @param args command-line arguments passed to the program
     */
    public static void main(String[] args) {
        ArgsController controller = new ArgsController();
        controller.run(args);
    }
}
