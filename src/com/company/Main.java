package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.*;

public class Main {
    static ArrayList<String> parents = new ArrayList<>();
    static ArrayList<String> names = new ArrayList<>();
    static HashMap<Integer, HashMap<Integer, HashMap<String, String>>> allElements =
            new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите значение, соответствующее нужному экрану для первой игры");
        System.out.println("buyTicket");
        System.out.println("mainscreen");
        System.out.println("ticket");
        System.out.println("ticketsController");
        String screen = sc.nextLine();
        switch (screen) {
            case "buyTicket":
                firstScreens(screen);
                break;
            case "mainscreen":
                firstScreens(screen);
                break;
            case "ticketsController":
                firstScreens(screen);
                break;
            case "ticket":
                resolveTicket("basePart");
                System.out.println("//Constraints for basePart");
                writeElements(allElements);
                parents = new ArrayList<>();
                names = new ArrayList<>();
                allElements = new HashMap<>();
                resolveTicket("interactionZone");
                System.out.println("//Constraints for interactionZone");
                writeElements(allElements);
                parents = new ArrayList<>();
                names = new ArrayList<>();
                allElements = new HashMap<>();
                resolveTicket("result");
                System.out.println("//Constraints for result");
                writeElements(allElements);
                parents = new ArrayList<>();
                names = new ArrayList<>();
                allElements = new HashMap<>();
                resolveTicket("multiplier");
                System.out.println("//Constraints for multiplier");
                writeElements(allElements);
                break;
        }


    }

    public static void resolveTicket(String name) {
        String jsonData = readFile("lotteries.json");
        JSONArray games = new JSONArray(jsonData);
        JSONObject firstgame = games.getJSONObject(14);//В теории, для других игр изменяем 0 на нужное число.
        //А потом молимся, чтобы ticketDataTemp не был null
        JSONObject ticketDataTemp = firstgame.getJSONObject("ticketDataTemp");
        JSONObject ticket = ticketDataTemp.getJSONObject("ticket");
        switch (name) {
            case "basePart":
                allElements.putAll(resolveBasePart(allElements.size(), name, ticket, allElements));
                break;
            case "multiplier":
                if (ticket.has("multiplier"))
                    allElements.putAll(resolveMultiplier(allElements.size(), name, ticket, allElements));
                else
                    System.out.println("//JSONArray [multiplier] is not found");
                break;
            case "interactionZone":
                allElements.putAll(resolveInteractionZone(allElements.size(), name, ticket, allElements));
                break;
            case "result":
                allElements.putAll(resolve3(allElements.size(), name, ticket, allElements));
                break;
        }

    }

    public static HashMap<Integer, HashMap<Integer, HashMap<String, String>>> resolveMultiplier(int al, String name, JSONObject ticket, HashMap<Integer, HashMap<Integer, HashMap<String, String>>> allElements) {
        HashMap<Integer, HashMap<String, String>> allMultiplierConstraints = new HashMap<>();

        JSONObject basePart = ticket.getJSONObject(name);

        //parse multiplier Constrains
        JSONArray basePartConstraints = basePart.getJSONArray("constraints");
        HashMap<Integer, HashMap<String, String>> constrains01 = parse(basePartConstraints);
        allElements.put(al, constrains01);
        parents.add("null");
        names.add("null");

        //parse multiplier Elements
        JSONArray multiplierElements = basePart.getJSONArray("elements");
        for (int k = 0; k < multiplierElements.length(); k++) {
            JSONObject layout = multiplierElements.getJSONObject(k);
            JSONArray constraints_json = layout.getJSONArray("constraints");
            parents.add(layout.get("parent_name").toString());
            names.add(layout.get("name").toString());
            HashMap<Integer, HashMap<String, String>> constrains0 = new HashMap<>();
            constrains0 = parse(constraints_json);
            try {
                JSONArray elements = layout.getJSONArray("elements");
                for (int l = 0; l < elements.length(); l++) {
                    JSONObject layout1 = elements.getJSONObject(l);
                    JSONArray constraints_json1 = layout1.getJSONArray("constraints");
                    parents.add(layout.get("parent_name").toString());
                    names.add(layout.get("name").toString());
                    HashMap<Integer, HashMap<String, String>> constrains011 = parse(constraints_json1);
                    al++;
                    allElements.put(al, constrains011);
                }
            } catch (JSONException l) {
                System.out.println("//JSONArray [elements] is not found");
            }
            al++;
            allElements.put(al, constrains0);
        }
        return allElements;
    }

    public static HashMap<Integer, HashMap<Integer, HashMap<String, String>>> resolveBasePart(int al, String name, JSONObject ticket, HashMap<Integer, HashMap<Integer, HashMap<String, String>>> allElements) {
        HashMap<Integer, HashMap<String, String>> allBasePartConstraints = new HashMap<>();

        JSONObject basePart = ticket.getJSONObject(name);
        JSONArray basePartConstraints = basePart.getJSONArray("constraints");
        parents.add("null");
        names.add("null");
        JSONArray basePartElements = basePart.getJSONArray("elements");
        for (int k = 0; k < basePartConstraints.length(); k++) {//parse BasePartConstraints
            JSONObject constrK = basePartConstraints.getJSONObject(k);
            HashMap<String, String> constrains = new HashMap<>();
            constrains.put("attribute1", constrK.get("attribute1").toString());
            constrains.put("attribute2", constrK.get("attribute2").toString());
            constrains.put("constant", constrK.get("constant").toString());
            constrains.put("from_object", constrK.get("from_object").toString());
            constrains.put("multiplier", constrK.get("multiplier").toString());
            constrains.put("offset", constrK.get("offset").toString());
            constrains.put("relation", constrK.get("relation").toString());
            constrains.put("to_object", constrK.get("to_object").toString());
            constrains.put("type", constrK.get("type").toString());
            allBasePartConstraints.put(k, constrains);
        }
        allElements.put(al, allBasePartConstraints);
        //parse basePartElements
        for (int k = 0; k < basePartElements.length(); k++) {
            JSONObject layout = basePartElements.getJSONObject(k);
            JSONArray constraints_json = layout.getJSONArray("constraints");
            parents.add(layout.get("parent_name").toString());
            names.add(layout.get("name").toString());
            HashMap<Integer, HashMap<String, String>> constrains0 = new HashMap<>();
            for (int i = 0; i < constraints_json.length(); i++) {
                JSONObject constrK = constraints_json.getJSONObject(i);
                HashMap<String, String> constrains = new HashMap<>();
                constrains.put("attribute1", constrK.get("attribute1").toString());
                constrains.put("attribute2", constrK.get("attribute2").toString());
                constrains.put("constant", constrK.get("constant").toString());
                constrains.put("from_object", constrK.get("from_object").toString());
                constrains.put("multiplier", constrK.get("multiplier").toString());
                constrains.put("offset", constrK.get("offset").toString());
                constrains.put("relation", constrK.get("relation").toString());
                constrains.put("to_object", constrK.get("to_object").toString());
                constrains.put("type", constrK.get("type").toString());
                constrains0.put(i, constrains);
            }
            al++;
            allElements.put(al, constrains0);
        }
        return allElements;
    }

    public static HashMap<Integer, HashMap<Integer, HashMap<String, String>>> resolveInteractionZone(int al, String name, JSONObject ticket, HashMap<Integer, HashMap<Integer, HashMap<String, String>>> allElements) {
        HashMap<Integer, HashMap<String, String>> allinteractionZoneConstraints = new HashMap<>();
        JSONObject interactionZone = ticket.getJSONObject(name);
        JSONArray interactionZoneConstraints = interactionZone.getJSONArray("constraints");
        parents.add("null");
        names.add("null");
        JSONArray interactionZoneElements = interactionZone.getJSONArray("elements");
        allinteractionZoneConstraints = parse(interactionZoneConstraints);
        allElements.put(al, allinteractionZoneConstraints);
        for (int k = 0; k < interactionZoneElements.length(); k++) {
            JSONObject layout = interactionZoneElements.getJSONObject(k);
            JSONArray constraints_json = layout.getJSONArray("constraints");
            parents.add(layout.get("parent_name").toString());
            names.add(layout.get("name").toString());
            HashMap<Integer, HashMap<String, String>> constrains0 = parse(constraints_json);
            al++;
            allElements.put(al, constrains0);
            try {
                JSONArray elements = layout.getJSONArray("elements");
                for (int l = 0; l < elements.length(); l++) {
                    JSONObject layout1 = elements.getJSONObject(l);
                    JSONArray constraints_json1 = layout1.getJSONArray("constraints");
                    parents.add(layout.get("parent_name").toString());
                    names.add(layout.get("name").toString());
                    HashMap<Integer, HashMap<String, String>> constrains01 = parse(constraints_json1);
                    al++;
                    allElements.put(al, constrains01);
                }
            } catch (JSONException l) {
                System.out.println("//JSONArray [elements] is not found");
            }
        }
        return allElements;
    }

    public static HashMap<Integer, HashMap<Integer, HashMap<String, String>>> resolve3(int al, String name, JSONObject ticket, HashMap<Integer, HashMap<Integer, HashMap<String, String>>> allElements) {
        HashMap<Integer, HashMap<String, String>> allresultConstraints = new HashMap<>();
        JSONObject result = ticket.getJSONObject(name);
        //парсим констрейны из резулта
        JSONArray resultConstraints = result.getJSONArray("constraints");
        allresultConstraints = parse(resultConstraints);
        parents.add("null");
        names.add("null");
        allElements.put(al, allresultConstraints);

        //парсим элементы из резулта
        JSONArray resultElements = result.getJSONArray("elements");
        for (int k = 0; k < resultElements.length(); k++) {
            JSONObject layout = resultElements.getJSONObject(k);
            JSONArray constraints_json = layout.getJSONArray("constraints");
            parents.add(layout.get("parent_name").toString());
            names.add(layout.get("name").toString());
            HashMap<Integer, HashMap<String, String>> constrains0 = parse(constraints_json);
            al++;
            allElements.put(al, constrains0);
        }


        //парсим win из резулта
        JSONObject winLayout = result.getJSONObject("win");
        JSONArray winElements = winLayout.getJSONArray("elements");
        System.out.println("//winElements " + winElements.length());
        for (int k = 0; k < winElements.length(); k++) {
            //      System.out.println("//WinElements[] "+k);
            JSONObject layout1 = winElements.getJSONObject(k);
            JSONArray constraints_json = layout1.getJSONArray("constraints");
            parents.add(layout1.get("parent_name").toString());
            names.add(layout1.get("name").toString());
            HashMap<Integer, HashMap<String, String>> constrains0 = parse(constraints_json);
            al++;
            allElements.put(al, constrains0);
        }

        //парсим lose из резулта
        JSONObject loseLayout = result.getJSONObject("lose");
        JSONArray loseElements = loseLayout.getJSONArray("elements");
        System.out.println("//LoseElements " + loseElements.length());
        for (int k = 0; k < loseElements.length(); k++) {
            //  System.out.println("//LoseElements[] "+k);
            JSONObject layout1 = loseElements.getJSONObject(k);
            JSONArray constraints_json = layout1.getJSONArray("constraints");
            parents.add(layout1.get("parent_name").toString());
            names.add(layout1.get("name").toString());
            HashMap<Integer, HashMap<String, String>> constrains0 = parse(constraints_json);
            al++;
            allElements.put(al, constrains0);
        }
        return allElements;
    }

    public static HashMap<Integer, HashMap<String, String>> parse(JSONArray constraints_json) {
        HashMap<Integer, HashMap<String, String>> constrains0 = new HashMap<>();
        for (int i = 0; i < constraints_json.length(); i++) {
            JSONObject constrK = constraints_json.getJSONObject(i);
            HashMap<String, String> constrains = new HashMap<>();
            try {
                constrains.put("attribute1", constrK.get("attribute1").toString());
            } catch (Exception l) {
                constrains.put("attribute1", "0");
            }
            try {
                constrains.put("attribute2", constrK.get("attribute2").toString());
            } catch (Exception l) {
                constrains.put("attribute2", "0");
            }
            try {
                constrains.put("constant", constrK.get("constant").toString());
            } catch (Exception l) {
                constrains.put("constant", "0");
            }
                constrains.put("from_object", constrK.get("from_object").toString());
            try {
                constrains.put("multiplier", constrK.get("multiplier").toString());
            } catch (Exception l) {
                constrains.put("multiplier", "0");
            }
            try {
                constrains.put("offset", constrK.get("offset").toString());
            } catch (Exception l) {
                constrains.put("offset", "0");
            }
            try {
                constrains.put("relation", constrK.get("relation").toString());
            } catch (Exception l) {
                constrains.put("relation", "0");
            }
                constrains.put("to_object", constrK.get("to_object").toString());
            try {
                constrains.put("type", constrK.get("type").toString());
            } catch (Exception l) {
                constrains.put("type", "0");
            }
            constrains0.put(i, constrains);
        }
        return constrains0;
    }

    public static void writeElements(HashMap<Integer, HashMap<Integer, HashMap<String, String>>> allElements) {
        for (int i = 0; i < allElements.size(); i++) {
            ConstraintsResolver constraintsResolver = new ConstraintsResolver(allElements.get(i), parents.get(i), names.get(i));
            ArrayList<String> constr = constraintsResolver.resolve();
            for (String s : constr)
                System.out.println("\"" + s + "\",");
        }
    }

    public static String readFile(String filename) {
        String result = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void firstScreens(String screen) {
        String jsonData = readFile("lotteries.json");
        JSONArray games = new JSONArray(jsonData);

        JSONObject firstgame = games.getJSONObject(1);//В теории, для других игр изменяем 0 на нужное число.
        //А потом молимся, чтобы ticketDataTemp не был null
        JSONObject ticketDataTemp = firstgame.getJSONObject("ticketDataTemp");
        JSONObject buyTicket = ticketDataTemp.getJSONObject(screen);//ticketDataTemp.getJSONObject("buyTicket");
        JSONArray elements = buyTicket.getJSONArray("elements");
        for (int i = 0; i < elements.length(); i++) {
            JSONObject layout = elements.getJSONObject(i);
            JSONArray constraints_json = layout.getJSONArray("constraints");
            parents.add(layout.get("parent_name").toString());
            names.add(layout.get("name").toString());
            HashMap<Integer, HashMap<String, String>> constrains0 = new HashMap<>();
            for (int k = 0; k < constraints_json.length(); k++) {
                JSONObject constrK = constraints_json.getJSONObject(k);
                HashMap<String, String> constrains = new HashMap<>();
                constrains.put("attribute1", constrK.get("attribute1").toString());
                constrains.put("attribute2", constrK.get("attribute2").toString());
                constrains.put("constant", constrK.get("constant").toString());
                constrains.put("from_object", constrK.get("from_object").toString());
                constrains.put("multiplier", constrK.get("multiplier").toString());
                constrains.put("offset", constrK.get("offset").toString());
                constrains.put("relation", constrK.get("relation").toString());
                constrains.put("to_object", constrK.get("to_object").toString());
                constrains.put("type", constrK.get("type").toString());
                constrains0.put(k, constrains);
            }
            allElements.put(i, constrains0);
        }
        for (int i = 0; i < allElements.size(); i++) {
            ConstraintsResolver constraintsResolver = new ConstraintsResolver(allElements.get(i), parents.get(i), names.get(i));
            ArrayList<String> constr = constraintsResolver.resolve();
            for (String s : constr)
                System.out.println("\"" + s + "\",");
        }
    }
}
