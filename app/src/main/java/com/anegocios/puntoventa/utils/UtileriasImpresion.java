package com.anegocios.puntoventa.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;


public class UtileriasImpresion {

    byte[] cc = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text
    byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
    byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
    byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
    byte[] open = {27, 112, 48, 55, 121};
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;


    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    boolean center;
    boolean dH;
    boolean dW;
    boolean bold;
    int milisegundos;
    int tipoLetra;
    String tipoImpresora;

    public boolean isCenter() {
        return center;
    }

    public void setCenter(boolean center) {
        this.center = center;
    }

    public boolean isdH() {
        return dH;
    }

    public void setdH(boolean dH) {
        this.dH = dH;
    }

    public boolean isdW() {
        return dW;
    }

    public void setdW(boolean dW) {
        this.dW = dW;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public int getTipoLetra() {
        return tipoLetra;
    }

    public void setTipoLetra(int tipoLetra) {
        this.tipoLetra = tipoLetra;
    }

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    /**
     * funcion para imprimir ticket en impresoras
     *
     * @param msg     mensaje que se va a imprimir
     * @param device  impresora donde se va a imprimir
     * @param tipoImp tipo de Impresora ( HOIN,GENE,las que vengan)
     * @return devuelve un error en caso de haberlo o una cadena vacia si se imprimio correctamente
     */
    public String imprimirTicket(String msg, BluetoothDevice device, String tipoImp, int milisegs) {
        String error = "";
        tipoImpresora = tipoImp;
        mmDevice = device;
        milisegundos = milisegs;
        error = openBT(msg);

        return error;
    }

    public String mandarLetra(BluetoothDevice device, String tipoImp, int milisegs) {
        String error = "";
        tipoImpresora = tipoImp;
        mmDevice = device;
        milisegundos = milisegs;
        if (tipoLetra == 1) {
            openBTMandarLetra(cc);
        } else if (tipoLetra == 2) {
            openBTMandarLetra(bb2);
        } else if (tipoLetra == 3) {
            openBTMandarLetra(bb3);
        } else if (tipoLetra == 0) {
            openBTMandarLetra(open);

        }


        return error;
    }

    public String imprimirImagen(byte[] msg, BluetoothDevice device, String tipoImp, int milisegs) {
        String error = "";
        tipoImpresora = tipoImp;
        mmDevice = device;
        milisegundos = milisegs;
        openBTImagen(msg);

        return error;
    }


    public String openBT(String msg) {
        String error = "";
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            error = beginListenForData();
            if (error.equals("")) {
                if (tipoImpresora.equals("HOIN")) {
                    error = sendDataHoin(msg);
                } else {
                    error = sendDataGenerico(msg);
                }
            }

            if (mmSocket.isConnected()) {
                mmSocket.close();
            }

        } catch (Exception e) {
            error = e.getMessage();
        } finally {
            if (mmSocket.isConnected()) {
                try {
                    mmSocket.close();
                } catch (Exception ex) {

                }
            }
        }
        return error;
    }


    public String openBTMandarLetra(byte[] msg) {
        String error = "";
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            error = beginListenForData();
            if (error.equals("")) {
                if (tipoImpresora.equals("HOIN")) {
                    // error=sendDataHoin(msg);
                } else {
                    error = sendDataGenericoLetra(msg);
                }
            }

            if (mmSocket.isConnected()) {
                mmSocket.close();
            }

        } catch (Exception e) {
            error = e.getMessage();
        } finally {
            if (mmSocket.isConnected()) {
                try {
                    mmSocket.close();
                } catch (Exception ex) {

                }
            }
        }
        return error;
    }


    public String openBTImagen(byte[] msg) {
        String error = "";
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            error = beginListenForData();
            if (error.equals("")) {
                if (tipoImpresora.equals("HOIN")) {
                    //error=sendDataHoin(msg);
                } else {
                    error = sendDataImagenGenerico(msg);
                }
            }

            if (mmSocket.isConnected()) {
                mmSocket.close();
            }

        } catch (Exception e) {
            error = e.getMessage();
        } finally {
            if (mmSocket.isConnected()) {
                try {
                    mmSocket.close();
                } catch (Exception ex) {

                }
            }
        }
        return error;
    }


    public String beginListenForData() {
        String error = "";
        try {


            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (Exception ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            error = e.getMessage();
        }
        return error;
    }

    private byte getFontCode(boolean doubleH, boolean doubleW, boolean bold) {
        int opCode = 0;
        if (bold) {
            opCode |= 8;
        }

        if (doubleH) {
            opCode |= 16;
        }

        if (doubleW) {
            opCode |= 32;
        }

        return (byte) opCode;
    }


    public String sendDataHoin(String msg) {
        String error = "";
        try {

          /*  byte[] centerData = new byte[]{27, 97, (byte) (center ? 1 : 0)};
            byte[] fontData = new byte[]{27, 33, getFontCode(dH, dW, bold)};

            mmOutputStream.write(centerData);
            mmOutputStream.write(fontData);
          */
          /*  try {
                Thread.sleep(milisegundos);
            } catch (InterruptedException e) {
            }*/

            byte[] send;
            try {
                send = msg.getBytes("GBK");
            } catch (UnsupportedEncodingException var6) {
                send = msg.getBytes();

            }

            mmOutputStream.write(send);

            byte[] tail = new byte[]{10, 13, 0};
            mmOutputStream.write(tail);

        } catch (Exception e) {
            error = e.getMessage();
        }
        return error;
    }


    public String sendDataGenerico(String msg) {

        String error = "";
        try {


            try {
                Thread.sleep(milisegundos);
            } catch (InterruptedException e) {
            }
            mmOutputStream.write(msg.getBytes());

        } catch (Exception e) {
            error = e.getMessage();
        }
        return error;
    }

    public String sendDataGenericoLetra(byte[] msg) {

        String error = "";
        try {


            try {
                Thread.sleep(milisegundos);
            } catch (InterruptedException e) {
            }


            mmOutputStream.write(msg);

        } catch (Exception e) {
            error = e.getMessage();
        }
        return error;
    }


    public String sendDataImagenGenerico(byte[] imagen) {

        String error = "";
        try {

            try {
                Thread.sleep(milisegundos);
            } catch (InterruptedException e) {
            }
            mmOutputStream.write(imagen);

        } catch (Exception e) {
            error = e.getMessage();
        }
        return error;
    }


}
