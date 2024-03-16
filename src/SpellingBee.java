// Isha Gupta 3/6/24

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);

    }
    public void makeWords(String word, String letters){
        if(!word.isEmpty()){
            words.add(word);
        }
        if(letters.isEmpty()){
            return;
        }
        for(int i = 0; i < letters.length(); i++){
            makeWords(word + letters.charAt(i), letters.substring(0, i) + letters.substring(i+1));
        }
    }


    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        mergeSplit(words);
    }
    public void mergeSplit(ArrayList<String> words){
        if(words.size() <= 1){
            return;
        }

        int midpoint = words.size() / 2;
        ArrayList<String> arr1 = new ArrayList<String>();
        ArrayList<String> arr2 = new ArrayList<String>();
        for(int i = 0; i < midpoint; i++){
            arr1.add(words.get(i));
        }
        for(int i = midpoint; i < words.size(); i++){
            arr2.add(words.get(i));
        }

        mergeSplit(arr1);
        mergeSplit(arr2);

        merge(words, arr1, arr2);
    }
    public void merge(ArrayList<String> words, ArrayList<String> arr1, ArrayList<String> arr2){
        // i is for arr1, j is for arr 2
        int i = 0, j = 0;
        while(i < arr1.size() && j < arr2.size()) {
            if (arr1.get(i).compareTo(arr2.get(j)) < 0) {
                words.set(i+j, arr1.get(i));
                i++;
            }
            else{
                words.set(i+j, arr2.get(j));
                j++;
            }
        }
        while(j < arr2.size()){
            words.set(i+j, arr2.get(j));
            j++;
        }
        while(i < arr1.size()){
            words.set(i+j, arr1.get(i));
            i++;
        }
    }
    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE

        int left,
                right,
                middle;
        boolean isWord;
        for(int i = 0; i < words.size(); i++){
            String word = words.get(i);
            isWord = false;
            left = 0;
            right = DICTIONARY_SIZE;
            middle = DICTIONARY_SIZE/2;

            while(left <= right){
                // if the word is left of word middle in dictionary
                if(DICTIONARY[middle].equals(word)){
                    isWord = true;
                    break;
                }
                else if(word.compareTo(DICTIONARY[middle]) < 0){
                    right = middle - 1;
                    middle = (right + left)/2;
                    //middle = (middle - left)/2 + left;
                }
                // (word.compareTo(DICTIONARY[middle]) > 0)
                else {
                    left = middle + 1;
                    middle = (right + left)/2;
                    //middle = (right - middle)/2 + middle;
                }
            }
            if(!isWord){
                words.remove(word);
                i--;
            }
        }
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
