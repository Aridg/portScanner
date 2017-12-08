package com.company.petrov.service;


import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class PortScanner {

    public static final int MIN_PORTS_PER_THREAD = 20;
    public static final int MAX_THREADS = 0xFF;

    InetAddress inetAddress;
    List<Integer> allPorts;

    List<Integer> allOpenPorts = new ArrayList<Integer>();
    List<PortScanWorker> workers = new ArrayList<PortScanWorker>(MAX_THREADS);

    private boolean isDone = false;
    Date startTime;
    Date endTime;

    private String host;
    private String ports;

    public PortScanner(String host) {
        this.host = host;
    }

    public PortScanner(String host, String ports) {
        this.host = host;
        this.ports = ports;
    }

    private void processArgs() {

        try {
            inetAddress = InetAddress.getByName(host);
        } catch (IOException ioe) {
            System.out.println("Error when resolving host!");
            System.exit(2);
        }

        System.out.println("Scanning host "+host);

        int minPort = 0;
        int maxPort = 0x10000-1;

        if (ports != null) {
            if (ports.indexOf("-")>-1) {
                // range of ports pointed out
                String[] ports = this.ports.split("-");
                try {
                    minPort = Integer.parseInt(ports[0]);
                    maxPort = Integer.parseInt(ports[1]);
                } catch (NumberFormatException nfe) {
                    System.out.println("Wrong ports!");
                    System.exit(3);
                }
            } else {
                // one port pointed out
                try {
                    minPort = Integer.parseInt(this.ports);
                    maxPort = minPort;
                } catch (NumberFormatException nfe) {
                    System.out.println("Wrong port!");
                    System.exit(3);
                }
            }
        }

        allPorts = new ArrayList<Integer>(maxPort-minPort+1);

        for (int i=minPort; i<=maxPort; i++) {
            allPorts.add(i);
        }
    }

    void usage() {
        System.out.println("Java Port Scanner usage: ");
        System.out.println("java Main host port");
        System.out.println("Examples:");
        System.out.println("java Main 192.168.1.1 1-1024");
        System.out.println("java Main 192.168.1.1 1099");
        System.out.println("java Main 192.168.1.1 (this scans all ports from 0 to 65535)");
    }

    public PortScanner scanPorts(){
        startTime = new Date();

        processArgs();

        if (allPorts.size()/MIN_PORTS_PER_THREAD > MAX_THREADS ) {
            final int PORTS_PER_THREAD = allPorts.size()/MAX_THREADS;

            List<Integer> threadPorts = new ArrayList<Integer>();
            for (int i=0,counter=0; i<allPorts.size();i++,counter++) {
                if (counter<PORTS_PER_THREAD) {
                    threadPorts.add(allPorts.get(i));
                } else {
                    PortScanWorker psw = new PortScanWorker();
                    psw.setInetAddress(inetAddress);
                    psw.setPorts(new ArrayList<Integer>(threadPorts));
                    workers.add(psw);
                    threadPorts.clear();
                    counter=0;
                }
            }
            PortScanWorker psw = new PortScanWorker();
            psw.setInetAddress(inetAddress);
            psw.setPorts(new ArrayList<Integer>(threadPorts));
            workers.add(psw);
        } else {
            List<Integer> threadPorts = new ArrayList<Integer>();
            for (int i=0,counter=0; i<allPorts.size();i++,counter++) {
                if (counter<MIN_PORTS_PER_THREAD) {
                    threadPorts.add(allPorts.get(i));
                } else {
                    PortScanWorker psw = new PortScanWorker();
                    psw.setInetAddress(inetAddress);
                    psw.setPorts(new ArrayList<Integer>(threadPorts));
                    workers.add(psw);
                    threadPorts.clear();
                    counter=0;
                }
            }
            PortScanWorker psw = new PortScanWorker();
            psw.setInetAddress(inetAddress);
            psw.setPorts(new ArrayList<Integer>(threadPorts));
            workers.add(psw);
        }

        System.out.println("Ports to scan: "+allPorts.size());
        System.out.println("Threads to work: "+workers.size());

        Runnable summarizer = new Runnable() {
            public void run()
            {
                System.out.println("Scanning stopped...");

                for (PortScanWorker psw : workers) {
                    List<Integer> openPorts = psw.getOpenPorts();
                    allOpenPorts.addAll(openPorts);
                }

                Collections.sort(allOpenPorts);

                System.out.println("List of opened ports:");
                for (Integer openedPort : allOpenPorts) {
                    System.out.println(openedPort);
                }

                endTime = new Date();

                System.out.println("Time of run: " + (endTime.getTime() - startTime.getTime()) + " ms");
                isDone = true;
            }
        };

        CyclicBarrier barrier = new CyclicBarrier(workers.size(),summarizer);

        for (PortScanWorker psw : workers) {
            psw.setBarrier(barrier);
        }

        System.out.println("Start scanning...");

        for (PortScanWorker psw : workers) {
            new Thread(psw).start();
        }

        return this;
    }

    public List<Integer> getAllOpenPorts() throws InterruptedException {
        while (!isDone)
            Thread.sleep(1000);
        return allOpenPorts;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getHost() {
        return host;
    }
}
