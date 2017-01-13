package hochschule.winki;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by danielf on 19.11.2016.
 */

public class Subjects {

    public static HashMap<String, String[]> getObjectMap() {
        HashMap<String, String[]> map = new HashMap<>();
        ArrayList<String[]> subjectlist = subjectList();
        ArrayList<ArrayList<String[]>> list = new ArrayList<>(7);
        list.add(firstSemesterList());
        list.add(secondSemesterList());
        list.add(thirdSemesterList());
        list.add(fourthSemesterList());
        list.add(sixthSemesterList());
        list.add(economisSemesterList());
        list.add(informaticsSemesterList());

        // Befüllen der Map mit allen Fächern als Key, und Value die einzelnen Begriffe
        for (int i = 0; i < subjectlist.size(); i++){
            int position = 0;
            for (String element: subjectlist.get(i)) {
                map.put(element, list.get(i).get(position));
                position++;
            }
        }
        return map;
    }

   /*
        //Alternative zum Befüllen der Map
        private ArrayList<String> allSubjectsList() {
        ArrayList<String> allSubjects = new ArrayList<>();
        for (String[] element: subjectList()) {

            for (int i = 0; i < element.length; i++) {
                allSubjects.add(element[i]);
            }
        }
        return allSubjects;
    }*/

    public static String[] firstSemester = {"Wirtschaftsmathematik 1", "BWL - Grundlagen", "Wirtschaftsinformatik 1", "Softwareentwicklung 1", "Buchführung"};
    public static final String[] secondSemester = {"Wirtschaftsprivatrecht 1", "Volkswirtschaft", "Statistik und Operations Research", "Wirtschaftsmathematik 2", "Softwarentwicklung 2", "Wirtschaftsinformatik 2"};
    public static final String[] thirdSemester = {"Personal und Organisation", "Datenbanksysteme", "Datenkommunikation", "Softwareengineering 1", "Informationssysteme 1"};
    public static final String[] fourthSemester = {"Bilanzierung und Steuern", "Informationssysteme 2", "Softwareengineering 2"};
    //private static final String[] fifthSemester = {"PBLV Seminar", "PBLV Projekt", "Praxisbericht"};
    public static final String[] sixthSemester = {"Kosten- Und Leistungsrechnung", "Business Simulation", "Geschäftsprozesse"};
    //private static final String[] seventhSemester = {"Bachelorarbeit"};
    public static final String[] wpfgWirtschaft = {"Produktionswirtschaft", "Datenschutz",  "Marketing"};
    public static final String[] wpfgIT = {"IT-Sicherheit", "Datenmanagement", "Algorithmen und Datenstrukturen"};
    public static ArrayList<String[]> subjectList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(firstSemester);
        list.add(secondSemester);
        list.add(thirdSemester);
        list.add(fourthSemester);
        //list.add(fifthSemester);
        list.add(sixthSemester);
        //list.add(seventhSemester);
        list.add(wpfgWirtschaft);
        list.add(wpfgIT);
        return list;
    }

    // 1. Semester
    private static final String[] wirtschaftsmathematik1 = {"Grenzwerte", "Funktion", "Umkehrfunktion"};
    private static final String[] bwl = {"Rechnungswesen", "Rechtsform", "Marketing"};
    private static final String[] wirtschaftsinformatik1 = {"Hardware", "Betriebssystem", "IT-Service-Management"};
    private static final String[] softwareentwicklung1 = {"Java", "Objektorientierte Programmierung", "Array"};
    private static final String[] bubi = {"Handelsgesetzbuch", "Umlaufvermögen", "Anlagevermögen"};
    private static ArrayList<String[]> firstSemesterList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(wirtschaftsmathematik1);
        list.add(bwl);
        list.add(wirtschaftsinformatik1);
        list.add(softwareentwicklung1);
        list.add(bubi);
        return list;
    }

    // 2. Semester
    private static final String[] wpr = {"Schadensersatz", "Kaufvertrag", "Rechtsfähigkeit"};
    private static final String[] vwl = {"Mikroökonomie", "Makroökonomie", "Bruttoinlandsprodukt"};
    private static final String[] statistik = {"Histogramm", "Korrelation", "Simplex-Verfahren"};
    private static final String[] wirtschaftsmathematik2 = {"Vektor", "Matrix", "Diferentialrechnung"};
    private static final String[] softwareentwicklung2 = {"Exception", "Vererbung", "Test-Driven-Development"};
    private static final String[] wirtschaftsinformatik2 = {"Cloud Computing", "BigDAta", "NoSQL"};
    private static ArrayList<String[]> secondSemesterList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(wpr);
        list.add(vwl);
        list.add(statistik);
        list.add(wirtschaftsmathematik2);
        list.add(softwareentwicklung2);
        list.add(wirtschaftsinformatik2);
        return list;
    }

    //3. Semester
    private static final String[] personalOrganisation = {"Aufbauorganisation", "Ablauforganisation", "Personalbeschaffung"};
    private static final String[] datenbanksysteme = {"Datenbankmanagementsystem", "SQL", "Datenunabhängigkeit"};
    private static final String[] datenkommunikation = {"Routing", "Subnetting", "TCP/IP"};
    private static final String[] softwareeningineering1 = {"Anforderungen", "Lastenheft", "UML"};
    private static final String[] informationssysteme1 = {"CRM", "ERP", "Systemarchitektur"};
    private static ArrayList<String[]> thirdSemesterList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(personalOrganisation);
        list.add(datenbanksysteme);
        list.add(datenkommunikation);
        list.add(softwareeningineering1);
        list.add(informationssysteme1);
        return list;
    }

    //4. Semester
    private static final String[] bilanzierungSteuern = {"Handelsbilanz", "Steuerbilanz", "Einkommenssteuer"};
    private static final String[] informationssysteme2 = {"Data-Mining", "SAP", "Clusteranalyse"};
    private static final String[] softwareengineering2 = {"Versionsverwaltung", "Kanban", "Scrum"};
    private static ArrayList<String[]> fourthSemesterList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(bilanzierungSteuern);
        list.add(informationssysteme2);
        list.add(softwareengineering2);
        return list;
    }

    //6. Semester
    private static final String[] klr = {"Gemeinkosten", "Einzelkosten", "Kostenstelle"};
    private static final String[] businessSimulation = {"Unternehmensbewertung", "Kleine und mittlere Unternehmen", "Startups"};
    private static final String[] processes = {"Geschäftsprozess", "Geschäftsprozessmodellierung", "BPMN"};
    private static ArrayList<String[]> sixthSemesterList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(klr);
        list.add(businessSimulation);
        list.add(processes);
        return list;
    }

    //Wahlpflichtfächergruppe Wirtschaft
    private static final String[] produktionswirtschaft = {"Insourcing", "Outsourcing", "Lean-Management"};
    private static final String[] datenschutz = {"Datenschutzbeauftragter", "TMG", "TKG"};
    private static final String[] marketing = {"Marketing", "Werbung", "Marketing-Mix"};
    private static ArrayList<String[]> economisSemesterList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(produktionswirtschaft);
        list.add(datenschutz);
        list.add(marketing);
        return list;
    }

    //Wahlpflichtfächergruppe IT
    private static final String[] itSicherheit = {"Sicherheitsmanagement", "Access Control", "Risikomanagement"};
    private static final String[] datenmanagement = {"OLAP", "OLTP", "Logdatei"};
    private static final String[] algoDat = {"Algorithmus", "Datenstruktur", "Compiler"};
    private static ArrayList<String[]> informaticsSemesterList() {
        ArrayList<String[]> list = new ArrayList<>();
        list.add(itSicherheit);
        list.add(datenmanagement);
        list.add(algoDat);
        return list;
    }

    public final static HashMap<String, String> getWikipediaTitle(){
        HashMap<String, String> map = new HashMap<>();
        map.put("Rechtsform", "Rechtsform");
        map.put("Rechnungswesen", "Rechnungswesen");
        map.put("Marketing", "Marketing");
        return map;
    }
}
