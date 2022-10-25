package com.github.jldelarbre.javaExperiments.reified_generics;

public class SpecialCharacters {

    public static void main(String[] args) {
        int [] codePoints = new int[Character.MAX_CODE_POINT];
        int count = 0;
        for (int codePoint = 0 ; codePoint < codePoints.length ; ++codePoint) {
            switch (Character.getType(codePoint)) {
                case Character.UNASSIGNED:
                case Character.CONTROL:
                case Character.FORMAT:
                case Character.PRIVATE_USE:
                case Character.SURROGATE:
                case Character.SPACE_SEPARATOR:
                case Character.LINE_SEPARATOR:
                case Character.PARAGRAPH_SEPARATOR:
                    break;
                default:
                    codePoints[count++] = codePoint;
            }
        }
        String chars = new String(codePoints, 0, count);
        
        System.out.println("Number of characters: " + count);
        System.out.println(chars);
    }
}
