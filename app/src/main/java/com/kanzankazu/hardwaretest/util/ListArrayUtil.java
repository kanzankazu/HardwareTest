package com.kanzankazu.hardwaretest.util;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListArrayUtil {

    public static void validationIdentityByType(EditText editText, String id, ArrayList<String> idIdentityTypeIdLv, ArrayList<String> idIdentityTypeMaxLenghtLv) {
        //editText.setText("");
        String s = editText.getText().toString();
        int i = s.length();

        for (int j = 0; j < idIdentityTypeIdLv.size(); j++) {
            if (id.equalsIgnoreCase("G0400")) {//TIDAK DI KETAHUI
                editText.setVisibility(View.INVISIBLE);
                editText.setText("");
            } else if (id.equalsIgnoreCase("G0401")) {//KTP
                int q = getPosStringInList(idIdentityTypeIdLv, "G0401");
                editText.setVisibility(View.VISIBLE);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(idIdentityTypeMaxLenghtLv.get(q)))});
                if (i > Integer.parseInt(idIdentityTypeMaxLenghtLv.get(q))) {
                    editText.setText("");
                }
            } else if (id.equalsIgnoreCase("G0402")) {//PASPOR
                int q = getPosStringInList(idIdentityTypeIdLv, "G0402");
                editText.setVisibility(View.VISIBLE);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(idIdentityTypeMaxLenghtLv.get(q)))});
                if (i > Integer.parseInt(idIdentityTypeMaxLenghtLv.get(q))) {
                    editText.setText("");
                }
            } else if (id.equalsIgnoreCase("G0403")) {//SIM
                int q = getPosStringInList(idIdentityTypeIdLv, "G0403");
                editText.setVisibility(View.VISIBLE);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(idIdentityTypeMaxLenghtLv.get(q)))});
                if (i > Integer.parseInt(idIdentityTypeMaxLenghtLv.get(q))) {
                    editText.setText("");
                }
            } else if (id.equalsIgnoreCase("G0404")) {//SIM RUSAK
                editText.setVisibility(View.INVISIBLE);
                editText.setText("");
            } else if (id.equalsIgnoreCase(idIdentityTypeIdLv.get(j))) {
                editText.setVisibility(View.VISIBLE);
                //editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(idIdentityTypeMaxLenghtLv.get(j)))});
                if (i > Integer.parseInt(idIdentityTypeMaxLenghtLv.get(j))) {
                    editText.setText("");
                }
            }
        }

        /*if (id.equalsIgnoreCase("G0400")) {//TIDAK DI KETAHUI
            editText.setVisibility(View.INVISIBLE);
            editText.setText("");
        } else if (id.equalsIgnoreCase("G0401")) {//KTP
            editText.setVisibility(View.VISIBLE);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            if (i > 16) {
                editText.setText("");
            }
        } else if (id.equalsIgnoreCase("G0402")) {//PASPOR
            editText.setVisibility(View.VISIBLE);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
            if (i > 9) {
                editText.setText("");
            }
        } else if (id.equalsIgnoreCase("G0403")) {//SIM
            editText.setVisibility(View.VISIBLE);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
            if (i > 12) {
                editText.setText("");
            }
        } else if (id.equalsIgnoreCase("G0404")) {//SIM RUSAK
            editText.setVisibility(View.INVISIBLE);
            editText.setText("");
        } else {
            editText.setVisibility(View.VISIBLE);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
            if (i > 50) {
                editText.setText("");
            }
        }*/
    }

    //int
    public static Integer[] convertListIntegertToIntegerArray(List<Integer> result) {
        Integer[] finalResult = new Integer[result.size()];
        return result.toArray(finalResult);
    }

    public static int[] convertListIntegertToIntArray(List<Integer> list) {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e.intValue();
        return ret;
    }

    @SuppressLint("NewApi")
    public static List<Integer> convertIntArrayToListIntegert(int[] data) {
        return Arrays.stream(data).boxed().collect(Collectors.toList());
    }

    @SuppressLint("NewApi")
    public static Integer[] convertIntArrayToIntegertArray(int[] data) {
        return Arrays.stream(data).boxed().toArray(Integer[]::new);
    }

    @SuppressLint("NewApi")
    public static int[] convertIntegertArrayToIntArray(Integer[] array) {
        return Arrays.stream(array).mapToInt(Integer::intValue).toArray();
    }

    //String
    public static ArrayList<String> convertStringArrayToListString(String[] strings) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            arrayList.add(strings[i]);
        }
        return arrayList;
    }

    public static String[] convertStringToStringArray(String s) {
        return s.split(",");
    }

    public static List<String> convertStringToListString(String s) {
        String[] strings = s.split(",");
        return Arrays.asList(strings);
    }

    public static boolean isIntArrayContainInt(final int[] array, final int key) {
        Arrays.sort(array);
        return Arrays.binarySearch(array, key) >= 0;
    }

    public static boolean isListContainString(List<String> sourceList, String s) {
        return sourceList.contains(s);
    }

    public static boolean isListContainStringArray(List<String> sourceList, String stringArray) {
        String[] strings = stringArray.split(",");
        List<String> stringList = Arrays.asList(strings);
        return sourceList.containsAll(stringList);
    }

    public static boolean isListContainStringList(List<String> sourceList, List<String> stringList) {
        return sourceList.containsAll(stringList);
    }

    public static List<Integer> getAllSizeStringList(List<String> sourceList) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            integers.add(i);
        }
        return integers;
    }

    public static int getPosStringInList(List<String> sourceList, String s) {
        int position = -1;
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).equalsIgnoreCase(s)) {
                position = i;
            }
        }
        return position;
    }

    public static List<Integer> getPosListStringInList(List<String> sourceList, String s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).equalsIgnoreCase(s)) {
                integers.add(i);
            }
        }
        return integers;
    }

    public static List<Integer> getInvPosListStringInList(List<String> sourceList, String s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            if (!sourceList.get(i).equalsIgnoreCase(s)) {
                integers.add(i);
            }
        }
        return integers;
    }

    public static List<Integer> getPosListStringIn2List(List<String> sourceList, List<String> s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            for (String ss : s) {
                if (sourceList.get(i).equalsIgnoreCase(ss)) {
                    integers.add(i);
                }
            }

        }
        return integers;
    }

    public static List<Integer> getInvPosListStringIn2List(List<String> sourceList, List<String> s) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < sourceList.size(); i++) {
            for (String ss : s) {
                if (!sourceList.get(i).equalsIgnoreCase(ss)) {
                    integers.add(i);
                }
            }

        }
        return integers;
    }

}
