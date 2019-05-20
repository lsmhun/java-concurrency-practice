package hu.lsm.concureny.practice;

import java.io.File;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class PrintingFilesRecursivelyInDirectory {

    private static class FilesPrinter extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private File directory;
        public FilesPrinter(File directory) {
            this.directory = directory;
        }

        @Override
        protected void compute() {
            for(File file : directory.listFiles()) {
                if(file.isDirectory()) {
                    new FilesPrinter(file).fork();
                } else {
                    System.out.println(file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String homeDir = "";
        if(args.length == 0){
            homeDir = System.getProperty("user.home");
        } else {
            homeDir = args[0];
        }
        final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.execute(new FilesPrinter(new File(homeDir)));

        forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
    }
}
