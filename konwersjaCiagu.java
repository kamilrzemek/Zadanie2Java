/**
	@author Kamil Rzemek
*/

import java.nio.file.Paths;
import java.util.*;
import java.io.*;
public class konwersjaCiagu {
    public static void main(String args[]) throws IOException {
        Scanner odczyt = new Scanner(Paths.get("cron.txt"), "UTF-8");
        String[] sheduleMinute = new String[100];
        String[] sheduleHour = new String[100];
        String[] sheduleDayOfMonth = new String[100];
        String[] sheduleDayOfWeek = new String[100];
        String wiersz;
        String[] rozdzialKreska = new String[4];
        StringTokenizer st, stgodzin;
        int index = 0;
        while (odczyt.hasNext()) {
            int indexKreska = 0;
            sheduleMinute[index] = "";
            sheduleHour[index] = "";
            sheduleDayOfMonth[index] = "";
            sheduleDayOfWeek[index] = "";
            wiersz = odczyt.nextLine();
            st = new StringTokenizer(wiersz, "|");
            //Mam wczytany wiersz, robie rozdzial delimem |
            while (st.hasMoreElements()) {
                rozdzialKreska[indexKreska] = st.nextElement().toString();
                indexKreska++;
            }
            //Sprawdzamy czy wczytano 4 informacje
            if (indexKreska < 4) {
                System.out.println("W wierszu " + (index + 1) + " wczytano za malo danych.");
            }
            else if (indexKreska > 4) {
                System.out.println("W wierszu " + (index + 1) + " wczytano za duzo danych.");
            }else {
                //cala reszta programu
                //minuty np. rozdzialKreska[0] = 10
                //godziny np. rozdzialKreska[1] = 10, 20, 21-23
                //dzienmiesiaca np. rozdzialKreska[2] = *
                //dzientygodnia np. rozdzialKreska[2] = 3-7

                //Sprawdzamy minuty
                if (rozdzialKreska[0].equals("*")) {
                    sheduleMinute[index] += "10 20 30 40 50 60";
                } else if (Integer.parseInt(rozdzialKreska[0]) >= 10 && Integer.parseInt(rozdzialKreska[0]) <= 60 && Integer.parseInt(rozdzialKreska[0]) % 10 == 0) {
                    sheduleMinute[index] += rozdzialKreska[0];
                } else {
                    System.out.println("W wierszu " + (index+1)+ " minuty podane niepoprawnie. Zly zakres lub niepoprawne znaki.");
                }
                //Sprawdzam godziny
                if (rozdzialKreska[1].equals("*")) {
                    sheduleHour[index] += "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23";
                } else {
                    //Dzielmy przecinkami
                    int indexGrP = 0;
                    String godzinyRozdzielonePrzecinkami[] = new String[24];
                    st = new StringTokenizer(rozdzialKreska[1], ",");
                    while (st.hasMoreElements()) {
                        godzinyRozdzielonePrzecinkami[indexGrP] = st.nextElement().toString();
                        indexGrP++;
                    }
//                    for(int b=0;b<indexGrP;b++){
//                        System.out.println(godzinyRozdzielonePrzecinkami[b]);
//                    }
                    //W tym momencie mam podstringi rozdzielone przecinkami np.
                    // 5 10 18-21
                    //Kazdy podstring musze rozdzielic myslnikiem, jesli myslnika nie ma to dodaje godzine
                    //A jesli myslnik jest, to dodaje kilka godzin

                    //Dzielimy myslnikami
                    for (int z = 0; z < indexGrP; z++) {
                        int indexGrM = 0;
                        String godzinyPodzieloneMyslnikiem[] = new String[2];
                        st = new StringTokenizer(godzinyRozdzielonePrzecinkami[z], "-");
                        //Mam rozdzielona pierwsza
                        while (st.hasMoreElements()) {
                            godzinyPodzieloneMyslnikiem[indexGrM] = st.nextElement().toString();
                            indexGrM++;
                        }
                        //Nie bylo myslnika
                        if (indexGrM == 1) {
                            //Sprawdzamy poprawnosc godziny i dodajemy do tablicy
                            if (Integer.parseInt(godzinyPodzieloneMyslnikiem[0]) >= 0 && Integer.parseInt(godzinyPodzieloneMyslnikiem[0]) <= 23) {
                                sheduleHour[index] += godzinyPodzieloneMyslnikiem[0];
                                sheduleHour[index] += " ";
                            } else {
                                System.out.println("Niepoprawna godzina w linijce" + (index + 1)+ " Zly zakres lub niepoprawne znaki.");
                            }
                        }
                        //Byl myslnik
                        else {
                            //Godzina przed myslnikiem jest mniejsza od godziny po myslniku
                            if (Integer.parseInt(godzinyPodzieloneMyslnikiem[0]) < Integer.parseInt(godzinyPodzieloneMyslnikiem[1])) {
                                //Obie sa dobrymi godzinami
                                if (Integer.parseInt(godzinyPodzieloneMyslnikiem[0]) >= 0 && Integer.parseInt(godzinyPodzieloneMyslnikiem[0]) <= 23
                                        && Integer.parseInt(godzinyPodzieloneMyslnikiem[1]) >= 0 && Integer.parseInt(godzinyPodzieloneMyslnikiem[1]) <= 23) {
                                    for (int y = Integer.parseInt(godzinyPodzieloneMyslnikiem[0]); y <= Integer.parseInt(godzinyPodzieloneMyslnikiem[1]); y++) {
                                        sheduleHour[index] += y;
                                        sheduleHour[index] += " ";
                                    }
                                }
                            }
                        }

                    }
                }
                //Sprawdzamy dni miesiaca
                if (rozdzialKreska[2].equals("*")) {
                    sheduleDayOfMonth[index] += "1 2 3 4 5 6 7 8 9 10 11 12 13 14";
                } else {
                    //Dzielmy przecinkami
                    int indexGrP = 0;
                    String dniRozdzielonePrzecinkami[] = new String[24];
                    st = new StringTokenizer(rozdzialKreska[2], ",");
                    while (st.hasMoreElements()) {
                        dniRozdzielonePrzecinkami[indexGrP] = st.nextElement().toString();
                        indexGrP++;
                    }
                    //Dzielimy myslnikami
                    for (int z = 0; z < indexGrP; z++) {
                        int indexGrM = 0;
                        String dniPodzieloneMyslnikiem[] = new String[2];
                        st = new StringTokenizer(dniRozdzielonePrzecinkami[z], "-");
                        //Mam rozdzielona pierwsza
                        while (st.hasMoreElements()) {
                            dniPodzieloneMyslnikiem[indexGrM] = st.nextElement().toString();
                            indexGrM++;
                        }
                        //Nie bylo myslnika
                        if (indexGrM == 1) {
                            //Sprawdzamy poprawnosc godziny i dodajemy do tablicy
                            if (Integer.parseInt(dniPodzieloneMyslnikiem[0]) >= 1 && Integer.parseInt(dniPodzieloneMyslnikiem[0]) <= 31) {
                                sheduleDayOfMonth[index] += dniPodzieloneMyslnikiem[0];
                                sheduleDayOfMonth[index] += " ";
                            } else {
                                System.out.println("Niepoprawna dni miesiaca w linijce" + (index + 1)+ " Zly zakres lub niepoprawne znaki.");
                            }
                        }
                        //Byl myslnik
                        else {
                            //Godzina przed myslnikiem jest mniejsza od godziny po myslniku
                            if (Integer.parseInt(dniPodzieloneMyslnikiem[0]) < Integer.parseInt(dniPodzieloneMyslnikiem[1])) {
                                //Obie sa dobrymi godzinami
                                if (Integer.parseInt(dniPodzieloneMyslnikiem[0]) >= 1 && Integer.parseInt(dniPodzieloneMyslnikiem[0]) <= 31
                                        && Integer.parseInt(dniPodzieloneMyslnikiem[1]) >= 1 && Integer.parseInt(dniPodzieloneMyslnikiem[1]) <= 31) {
                                    for (int y = Integer.parseInt(dniPodzieloneMyslnikiem[0]); y <= Integer.parseInt(dniPodzieloneMyslnikiem[1]); y++) {
                                        sheduleDayOfMonth[index] += y;
                                        sheduleDayOfMonth[index] += " ";
                                    }
                                }
                            }
                        }
                    }
                }

                //Sprawdzamy dni tygodnia
                if (rozdzialKreska[3].equals("*")) {
                    sheduleDayOfWeek[index] += "1 2 3 4 5 6 7";
                } else {
                    //Dzielmy przecinkami
                    int indexGrP = 0;
                    String dniTRozdzielonePrzecinkami[] = new String[24];
                    st = new StringTokenizer(rozdzialKreska[3], ",");
                    while (st.hasMoreElements()) {
                        dniTRozdzielonePrzecinkami[indexGrP] = st.nextElement().toString();
                        indexGrP++;
                    }
                    //Dzielimy myslnikami
                    for (int z = 0; z < indexGrP; z++) {
                        int indexGrM = 0;
                        String dniTPodzieloneMyslnikiem[] = new String[2];
                        st = new StringTokenizer(dniTRozdzielonePrzecinkami[z], "-");
                        //Mam rozdzielona pierwsza
                        while (st.hasMoreElements()) {
                            dniTPodzieloneMyslnikiem[indexGrM] = st.nextElement().toString();
                            indexGrM++;
                        }
                        //Nie bylo myslnika
                        if (indexGrM == 1) {
                            //Sprawdzamy poprawnosc godziny i dodajemy do tablicy
                            if (Integer.parseInt(dniTPodzieloneMyslnikiem[0]) >= 1 && Integer.parseInt(dniTPodzieloneMyslnikiem[0]) <= 7) {
                                sheduleDayOfWeek[index] += dniTPodzieloneMyslnikiem[0];
                                sheduleDayOfWeek[index] += " ";
                            } else {
                                System.out.println("Niepoprawne dni tygodnia w linijce" + (index + 1)+ " Zly zakres lub niepoprawne znaki.");
                            }
                        }
                        //Byl myslnik
                        else {
                            //Godzina przed myslnikiem jest mniejsza od godziny po myslniku
                            if (Integer.parseInt(dniTPodzieloneMyslnikiem[0]) < Integer.parseInt(dniTPodzieloneMyslnikiem[1])) {
                                //Obie sa dobrymi godzinami
                                if (Integer.parseInt(dniTPodzieloneMyslnikiem[0]) >= 1 && Integer.parseInt(dniTPodzieloneMyslnikiem[0]) <= 8
                                        && Integer.parseInt(dniTPodzieloneMyslnikiem[1]) >= 1 && Integer.parseInt(dniTPodzieloneMyslnikiem[1]) <= 7) {
                                    for (int y = Integer.parseInt(dniTPodzieloneMyslnikiem[0]); y <= Integer.parseInt(dniTPodzieloneMyslnikiem[1]); y++) {
                                        sheduleDayOfWeek[index] += y;
                                        sheduleDayOfWeek[index] += " ";
                                    }
                                }
                            }
                        }
                    }
                }

            }
            index++;
        }
        for(int i=0;i<index;i++){
            System.out.println("Index "+ (i+1));
            System.out.println("Minuty "+ sheduleMinute[i]);
            System.out.println("Godziny "+ sheduleHour[i]);
            System.out.println("Dni miesiaca "+ sheduleDayOfMonth[i]);
            System.out.println("Dni tygodnia "+ sheduleDayOfWeek[i]);
            System.out.println();
        }
    }
}
