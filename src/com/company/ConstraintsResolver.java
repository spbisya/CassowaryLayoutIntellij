package com.company;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 777 on 1/19/2016.
 */
public class ConstraintsResolver {
    String viewTag = null;
    HashMap<Integer, HashMap<String, String>> allConstraints = new HashMap<>();
    ArrayList<String> constrains = new ArrayList<>();
    int attr1, attr2, constant, offset, relation, type;
    String multiplier;
    String from_object, to_object;
    Boolean height = false, width = false, top = false, right = false, left = false, bottom = false;
    String parent = "",name = "";


    public ConstraintsResolver(HashMap<Integer, HashMap<String, String>> allConstraints, String parent,String name) {
        this.allConstraints = allConstraints;
        this.parent = parent;
        this.name = name;
    }


    public ArrayList<String> resolve() {
        constrains = new ArrayList<>();
        for (int i = 0; i < allConstraints.size(); i++) {
            HashMap<String, String> constrainI = allConstraints.get(i);
            attr1 = Integer.parseInt(constrainI.get("attribute1"));
            attr2 = Integer.parseInt(constrainI.get("attribute2"));
            constant = Integer.parseInt(constrainI.get("constant"));
            from_object = constrainI.get("from_object");
            multiplier = constrainI.get("multiplier");
            offset = Integer.parseInt(constrainI.get("offset"));
            relation = Integer.parseInt(constrainI.get("relation"));
            to_object = constrainI.get("to_object");
            type = Integer.parseInt(constrainI.get("type"));
            viewTag = from_object;
            if (parent.equals("null")) parent = "container";
            if (to_object.equals("superView")||to_object.equals("null")) to_object = "container";
            typeResolver(type);
        }
        if (height == false) {
            if (top == false | bottom == false)
                if(name.equals("null")) {
                    constrains.add(viewTag + ".height==40dp");
                }
            else{
                    constrains.add(name + ".height==40dp");
                }
        }
        if (width == false) {
            if (left == false | right == false)
                if(name.equals("null")) {
                constrains.add(viewTag + ".width==" + parent + ".width");
                }
                else{
                    constrains.add(name + ".width==" + parent + ".width");
                }
        }
        return constrains;
    }

    public void typeResolver(int type) {
        switch (type) {
            case 0:
                //   не нашёл в json
                break;
            case 1:
                constrains.add(resolveType1(attr1, constant));
                break;
            case 2:
                if (to_object.equals("container"))
                    constrains.add(resolveType2Container(attr1, attr2, constant));
                else
                    constrains.add(resolveType2(attr1, attr2, constant));
                break;
            case 3:
                constrains.add(resolveType3(attr1, constant));
                break;
            case 4:
                constrains.add(from_object + ".x == 0");
                constrains.add(from_object + ".y == 0");
                constrains.add(from_object + ".y2==" + parent + ".height");
                constrains.add(from_object + ".x2==" + parent + ".width");
                height = true;
                width = true;
                break;
            case 5:
                constrains.add(resolveType5(attr1, attr2));
                break;
            case 6:
                constrains.add(from_object + ".centerY==" + parent + ".centerY");
                constrains.add(from_object + ".centerX==" + parent + ".centerX");
                break;
            case 7:
                constrains.add(resolveType7(attr1,constant));
                break;
            case 8:
                constrains.add(resolveType8(attr1, attr2, multiplier));
                break;
            case 9:
             //   Не нашёл в json
                break;
        }

    }

    public String resolveType2(int attr1, int attr2, int constant) {
        switch (attr1) {
            case 3:
                top = true;
                String s = from_object + ".top==";
                switch (attr2) {
                    case 3:
                        s += to_object + ".top" + ((constant >= 0) ? "+" + constant : "" + constant) + "dp";
                        break;
                    case 4:
                        s += to_object + ".bottom" + ((constant >= 0) ? "+" + constant : "" + constant) + "dp";
                        break;

                }
                if (attr1 > 0)
                    return s;
                break;
            case 4:
                bottom = true;
                String l = from_object + ".bottom==";

                switch (attr2) {
                    case 3:
                        l += to_object + ".top" + ((constant >= 0) ? "+" + constant : "" + constant) + "dp";
                        break;
                    case 4:
                        l += to_object + ".bottom" + ((constant >= 0) ? "+" + constant : "" + constant) + "dp";
                        break;
                }

                if (attr1 > 0)
                    return l;
                break;
            case 5:
                left = true;
                String five = from_object + ".left==";
                switch (attr2) {
                    case 5:
                        five += to_object + ".left" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                    case 6:
                        five += to_object + ".right" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                }
                if (attr1 > 0)
                    return five;
                break;
            case 6:
                right = true;
                String six = from_object + ".right==";
                switch (attr2) {
                    case 5:
                        six += to_object + ".left" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                    case 6:
                        six += to_object + ".right" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                }
                if (attr1 > 0)
                    return six;
                break;
        }
        return "1";
    }

    public String resolveType2Container(int attr1, int attr2, int constant) {
        switch (attr1) {
            case 3:
                top = true;
                String s = from_object + ".top==";

                switch (attr2) {
                    case 3:
                        s += "" + constant + "dp";
                        break;
                    case 4:
                        s += "container.height" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                }
                if (attr1 > 0)
                    return s;
                break;
            case 4:
                bottom = true;
                String l = from_object + ".bottom==";
                switch (attr2) {
                    case 3:
                        l += "" + constant + "dp";
                        break;
                    case 4:
                        l += "container.height" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                }
                if (attr1 > 0)
                    return l;
                break;
            case 5:
                left = true;
                String five = from_object + ".left==";
                switch (attr2) {
                    case 5:
                        five += to_object + ".left" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                    case 6:
                        five += to_object + ".right" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                }
                if (attr1 > 0)
                    return five;
                break;
            case 6:
                right = true;
                String six = from_object + ".right==";
                switch (attr2) {
                    case 5:
                        six += to_object + ".left" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                    case 6:
                        six += to_object + ".right" + ((constant >= 0) ? ("+" + constant) : "" + constant) + "dp";
                        break;
                }
                if (attr1 > 0)
                    return six;
                break;
        }
        return "3";
    }

    public String resolveType8(int attr1, int attr2, String multiplier) {
        switch (attr1) {
            case 8:
                height = true;
                String s = from_object + ".height==";
                switch (attr2) {
                    case 7:
                        s += to_object + ".width*" + multiplier;
                        if (attr1 > 0)
                            return s;
                        break;
                    case 8:
                        s += to_object + ".height*" + multiplier;
                        if (attr1 > 0)
                            return s;
                        break;
                }
                break;
            case 7:
                width = true;
                String l = from_object + ".width==";
                switch (attr2) {
                    case 7:
                        l += to_object + ".width*" + multiplier;
                        if (attr1 > 0)
                            return l;
                        break;
                    case 8:
                        l += to_object + ".height*" + multiplier;
                        if (attr1 > 0)
                            return l;
                        break;
                }
                break;
        }
        return "4";
    }

    public String resolveType1(int attr1, int constant) {
        switch (attr1) {
            case 7:
                width = true;
                if (attr1 > 0)
                    return from_object + ".width==" + constant + "dp";
                break;
            case 8:
                height = true;
                if (attr1 > 0)
                    return from_object + ".height==" + constant + "dp";
                break;
        }
        return "5";
    }

    public String resolveType3(int attr1, int constant) {
        switch (attr1) {
            case 9:
                if (attr1 > 0)
                    return from_object + ".centerX==" + parent + ".centerX" + ((constant >= 0) ? "+" + constant : "" + constant) + "dp";
                break;
            case 10:
                if (attr1 > 0)
                    return from_object + ".centerY==" + parent + ".centerY" + ((constant >= 0) ? "+" + constant : "" + constant) + "dp";
                break;
        }
        return "6";
    }

    public String resolveType5(int attr1, int attr2) {
        switch (attr1) {
            case 6:
                right = true;
                String s = from_object + ".right==";
                switch (attr2) {
                    case 6:
                        s += to_object + ".right";
                        if (attr1 > 0) return s;
                        break;
                }
                break;
            case 8:
                height = true;
                String l = from_object + ".height==";
                switch (attr2) {
                    case 8:
                        l += to_object + ".height";
                        if (attr1 > 0) return l;
                        break;
                }
                break;
        }
        return "7";
    }

    public String resolveType7(int attr1, int constant) {
        switch (attr1) {
            case 3:
                top=true;
                if (attr1 > 0)
                    return from_object + ".top=="+((constant >= 0) ? "" + constant : "" + constant) + "dp";
                break;
            case 4:
                bottom=true;
                if (attr1 > 0)
                    return from_object + ".bottom==" + to_object + ".height" + ((constant >= 0) ? "-" + constant : "+" + Math.abs(constant)) + "dp";
                break;
            case 5:
                left=true;
                if (attr1 > 0) return from_object + ".left==" + constant + "dp";
                break;
            case 6:
                right=true;
                if (attr1 > 0)
                    return from_object + ".right==" + to_object + ".width" + ((constant >= 0) ? "-" + constant : "+" + Math.abs(constant)) + "dp";
                break;
        }
        return "8";
    }
}
