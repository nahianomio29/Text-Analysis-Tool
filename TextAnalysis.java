import java.io.*;
import java.util.Scanner;
// import java.util.ArrayList;

// analyzer interface
interface Analyzer {
    void analyzeText(); // analyze text
    void printReport(); // print report
    void saveReport(String filename); // save report
}

// base class
abstract class TextAnalysisBase {
    protected String text; // analyzing text
    protected String[] stopwords; // store list of stopwords
    protected String[] positive; // store list of positive words
    protected String[] negative; // store list of negative words

    // constructor
    public TextAnalysisBase(String text, String[] stopwords, String[] positive, String[] negative)
    {
        this.text = text; // pass text
        this.stopwords = stopwords; // pass stopwords
        this.positive = positive; // pass positive words
        this.negative = negative; // pass negative words
    }

    // method to check if word is in the stopword list
    protected boolean isStopword(String word)
    {
        // loop to go through each stopword
        for (int i = 0; i < stopwords.length; i++)
        {
            if (stopwords[i].equals(word))
            {
                return true; // if found, return true
            }
        }
        return false; // else return false
    }

    // method to check if specific word exists in given array
    protected boolean containWord(String word, String[] arr)
    {
        // loop to go through each word
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i].equals(word))
            {
                return true; // return true if matches
            }
        }
        return false; // else return false
    }

    // method to count vowels
    protected int countVowels(String s)
    {
        int count = 0; // start from 0
        // loop to go through each character
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') // if these exist
            {
                count++; // increment vowel count
            }
        }
        return count; // return vowel count
    }

    // method to count consonants
    protected int countConsonants(String s)
    {
        int count = 0; // start from 0
        // loop to go through each character
        for (int i = 0; i < s.length(); i++)
        {
            char c = Character.toLowerCase(s.charAt(i)); // convert all letters to lower case
            boolean isLetter = (c >= 'a' && c <= 'z'); // letter if in between a to z
            boolean isVowel = (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u');
            if (isLetter && !isVowel) // if not vowel, then consonant
            {
                count++; // increment consonant count
            }
        }
        return count; // return consonant count
    }
}

// subclass - LiteraryAnalysis, extends base class and implements interface
class LiteraryAnalysis extends TextAnalysisBase implements Analyzer
{
    // store words in array
    private String[] uniqueWords = new String[5000]; // store unique words
    private int[] frequency = new int[5000]; // store how many times a word is there
    private int uniqueCount = 0; // count number of unique words

    // store analysis result
    private int totalWords; // store total words without including stopwords
    private int totalSentences; // store total number of sentences
    private int totalParagraphs; // store total number of paragraphs
    private int totalCharacters; // store total number of characters
    private int vowels; // store total number of vowels
    private int consonants; // store total number of consonants
    private double vocabularyRichness; // store vocabulary richness (number of unique words / total words)
    private String mostFrequentWord; // store most frequent word
    private int mostFrequentWordCount; // store most frequent word count
    private String longestWord; // store longest word
    private String[] top20Words = new String[20]; // store top 20 most frequent words
    private int top20WordCount; // store top 20 most frequent words count
    private String sentiment; // store sentiment

    // pass constructor data
    public LiteraryAnalysis(String text, String[] stopwords, String[] positive, String[] negative)
    {
        super(text, stopwords, positive, negative);
    }

    // perform analysis
    public void analyzeText()
    {
        text = text.replace("\r", ""); // clean up text

        // count sentences
        String[] sentences = text.split("[.!?]"); // split sentence by period, exclamation and question mark

        totalSentences = 0; // start from 0
        for (int i = 0; i < sentences.length; i++)
        {
            // loop to go through each sentence
            if (!sentences[i].trim().isEmpty()) // if string is not empty
            {
                totalSentences++; // count increment
            }
        }

        // count paragraphs
        String[] paragraphs = text.split("\n\n"); // split paragraph by blank lines

        totalParagraphs = 0; // start from zero
        for (int i = 0; i < paragraphs.length; i++)
        {
            if (!paragraphs[i].trim().isEmpty()) // if not empty
            {
                totalParagraphs++; // increase count
            }
        }

        // count vowels and consonants
        vowels = countVowels(text);
        consonants = countConsonants(text);

        // process words
        String plainText = text.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", " "); // remove symbols
        String[] words = plainText.split("\\s+"); // split by spaces

        totalWords = 0; // total word count, start from 0
        totalCharacters = 0; // total character count, start from 0

        // count words
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i].trim(); // remove spaces

            if (word.isEmpty())
            {
                continue; // if empty, skip
            }

            if (isStopword(word))
            {
                continue; // if stopword, skip
            }

            totalWords++; // increment total word count
            addWord(word); // add word to list

            // count characters
            for (int j = 0; j < word.length(); j++)
            {
                char c = word.charAt(j);
                if (Character.isLetter(c) || Character.isDigit(c)) // count if only letter or digit
                {
                    totalCharacters++; // count increment
                }
            }
        }

        // calculate vocabulary richness
        if (totalWords > 0)
        {
            vocabularyRichness = (double) uniqueCount / totalWords; // vocabulary richness formula
        }
        else
        {
            vocabularyRichness = 0.0;
        }

        // analysis details
        findMostFrequentWord(); // find most frequent word
        findLongestWord(); // find the longest word
        findTopWords(); // find top 20 most frequent words
        findSentiment(); // find the sentiment of the text
    }

    // method to add word or increase frequency count
    private void addWord(String word)
    {
        // loop through text to check if word already exists
        for (int i = 0; i < uniqueCount; i++)
        {
            if (uniqueWords[i].equals(word)) // if word already exists
            {
                frequency[i]++; // increase frequency count of that word
                return;
            }
        }
        uniqueWords[uniqueCount] = word; // add the new word
        frequency[uniqueCount] = 1; // set frequency count to 1
        uniqueCount++; // increase count
    }

    // method to find most frequent word
    private void findMostFrequentWord()
    {
        mostFrequentWordCount = 0; // start from 0
        mostFrequentWord = ""; // start from empty
        // loop through the words

        for (int i = 0; i < uniqueCount; i++)
        {
            // if current word frequency is more than the highest frequency
            if (frequency[i] > mostFrequentWordCount)
            {
                mostFrequentWordCount = frequency[i]; // update highest frequency
                mostFrequentWord = uniqueWords[i]; // make the word the most frequent one
            }
        }
    }

    // Find the longest word
    private void findLongestWord()
    {
        longestWord = ""; // start from empty
        for (int i = 0; i < uniqueCount; i++)
        {
            // if word length is greater than the existing longest word
            if (uniqueWords[i].length() > longestWord.length())
            {
                longestWord = uniqueWords[i]; // update longest word
            }
        }
    }

    // method to update sentiment
    private void findSentiment()
    {
        int positiveCount = 0; // start from zero
        int negativeCount = 0; // start from zero

        for (int i = 0; i < uniqueCount; i++)
        {
            if (containWord(uniqueWords[i], positive))
            {
                positiveCount += frequency[i]; // count positive words
            }
            if (containWord(uniqueWords[i], negative))
            {
                negativeCount += frequency[i]; // count negative words
            }
        }

        if (positiveCount > negativeCount) // if positive word count is greater than negative
        {
            sentiment = "Positive"; // sentiment is positive
        }
        else if (negativeCount > positiveCount) // if negative word count is greater than positive
        {
            sentiment = "Negative"; // sentiment is negative
        }
        else
        {
            sentiment = "Neutral"; // otherwise sentiment is neutral
        }
    }

    // method to find top 20 words
    private void findTopWords()
    {
        String[] wordsCopy = new String[uniqueCount]; // create array to store copied words
        int[] frequencyCopy = new int[uniqueCount]; // create array to store copied frequency

        // loop to go through each unique word and their frequency
        for (int i = 0; i < uniqueCount; i++)
        {
            wordsCopy[i] = uniqueWords[i]; // copy words
            frequencyCopy[i] = frequency[i]; // copy frequency
        }

        // bubble sort to sort by frequency (highest first)
        for (int i = 0; i < uniqueCount - 1; i++)
        {
            for (int j = i + 1; j < uniqueCount; j++)
            {
                // if current word has more frequency count, swap
                if (frequencyCopy[j] > frequencyCopy[i])
                {
                    // swap frequency
                    int temp = frequencyCopy[i];
                    frequencyCopy[i] = frequencyCopy[j];
                    frequencyCopy[j] = temp;

                    // swap words
                    String tempWord = wordsCopy[i];
                    wordsCopy[i] = wordsCopy[j];
                    wordsCopy[j] = tempWord;
                }
            }
        }

        // get top 20 words
        top20WordCount = Math.min(uniqueCount, 20);
        for (int i = 0; i < top20WordCount; i++)
        {
            top20Words[i] = wordsCopy[i] + ": " + frequencyCopy[i]; // store word with their
        }
    }

    // print report
    public void printReport()
    {
        System.out.println("=== Literary Analysis Report ===");
        System.out.println("Total Words (excluding stopwords): " + totalWords);
        System.out.println("Total Sentences: " + totalSentences);
        System.out.println("Total Paragraphs: " + totalParagraphs);
        System.out.println("Total Characters: " + totalCharacters);
        System.out.println("Unique Words: " + uniqueCount);
        System.out.printf("Vocabulary Richness: %.3f\n", vocabularyRichness);
        System.out.println("Most Frequent Word: " + mostFrequentWord + " (" + mostFrequentWordCount + ")");
        System.out.println("Longest Word: " + longestWord);
        System.out.println("Vowels: " + vowels);
        System.out.println("Consonants: " + consonants);
        System.out.println("Sentiment: " + sentiment);
        System.out.println();
        System.out.println("Top 20 Words:");

        for (int i = 0; i < top20WordCount; i++)
        {
            System.out.println(top20Words[i]);
        }
    }

    // print and save to .txt file
    public void saveReport(String filename)
    {
        try
        {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.println("=== Literary Analysis Report ===");
            writer.println("Total Words (excluding stopwords): " + totalWords);
            writer.println("Total Sentences: " + totalSentences);
            writer.println("Total Paragraphs: " + totalParagraphs);
            writer.println("Total Characters: " + totalCharacters);
            writer.println("Unique Words: " + uniqueCount);
            writer.printf("Vocabulary Richness: %.3f\n", vocabularyRichness);
            writer.println("Most Frequent Word: " + mostFrequentWord + " (" + mostFrequentWordCount + ")");
            writer.println("Longest Word: " + longestWord);
            writer.println("Vowels: " + vowels);
            writer.println("Consonants: " + consonants);
            writer.println("Sentiment: " + sentiment);
            writer.println();
            writer.println("Top 20 Words:");

            for (int i = 0; i < top20WordCount; i++)
            {
                writer.println(top20Words[i]);
            }
            writer.close();
        }
        // catch exception if any error
        catch (IOException e)
        {
            System.out.println("Error: " + e);
        }
    }

    // getters
    public String[] getUniqueWords()
    {
        return uniqueWords;
    }

    public int[] getFrequencies()
    {
        return frequency;
    }

    public int getUniqueCount()
    {
        return uniqueCount;
    }
}

// class FileHandler to handle file reading and error management.
class FileHandler
{
    // read .txt file
    public static String readFile(String path)
    {
        StringBuilder textData = new StringBuilder();
        try (Scanner fileScanner = new Scanner(new File(path)))
        {
            while (fileScanner.hasNextLine())
            {
                textData.append(fileScanner.nextLine()).append("\n");
            }
        }
        catch (IOException e)
        {
            System.out.println("Error: Path not found " + path);
        }
        return textData.toString();
    }

    // load .txt word files
    public static String[] loadWords(String filename)
    {
        String[] temp = new String[5000]; // create array with 5000 words
        int count = 0; // start from 0

        try
        {
            Scanner scanner = new Scanner(new File(filename));  // open file

            // loop to go through each line upto 5000
            while (scanner.hasNextLine() && count < 5000)
            {
                String line = scanner.nextLine().trim().toLowerCase();  // convert to lowercase
                temp[count] = line;  // store words
                count++; // count increment
            }
            scanner.close(); // close scanner
        }
        // catch exception
        catch (IOException e)
        {
            System.out.println("Error: " + filename);
        }
        // create new array with same size
        String[] result = new String[count];
        for (int i = 0; i < count; i++)
        {
            result[i] = temp[i];
        }
        return result; // return words
    }
}

// method to calculate similarity
class SimilarityCalculator
{
    public static double cosineSimilarity(LiteraryAnalysis text1, LiteraryAnalysis text2)
    {
        // get words and frequencies
        String[] words1 = text1.getUniqueWords(); // create new array for words from first text file
        int[] frequency1 = text1.getFrequencies(); // create new array for frequency from first text file
        int count1 = text1.getUniqueCount(); // create new array for word count from first text file

        String[] words2 = text2.getUniqueWords(); // create new array for words from second text file
        int[] frequency2 = text2.getFrequencies(); // create new array for frequency from second text file
        int count2 = text2.getUniqueCount(); // create new array for word count from second text file

        double dotProduct = 0.0; // store dot product, start from 0
        double magnitude1 = 0.0; // store magnitude/length from first text file
        double magnitude2 = 0.0; // store magnitude/length from second text file

        // calculate dot product and magnitude for first text file
        for (int i = 0; i < count1; i++)
        {
            magnitude1 += frequency1[i] * frequency1[i];
            // find similarity from second text file
            for (int j = 0; j < count2; j++)
            {
                if (words1[i] != null && words1[i].equals(words2[j]))
                {
                    dotProduct += frequency1[i] * frequency2[j];
                }
            }
        }

        // calculate magnitude from second text file
        for (int j = 0; j < count2; j++)
        {
            magnitude2 += frequency2[j] * frequency2[j];
        }

        // if magnitude is 0, return 0
        if (magnitude1 == 0 || magnitude2 == 0)
        {
            return 0.0;
        }
        // cosine similarity formula
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }
}

// main class
public class TextAnalysis {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // get user input for .txt files
        System.out.print("Enter the path for the first text file: ");
        String file1 = scanner.nextLine();
        System.out.print("Enter the path for the second text file: ");
        String file2 = scanner.nextLine();

        // load .txt files for stopwords, positive words and negative words
        String[] stopwords = FileHandler.loadWords("stopwords.txt");
        String[] positiveWords = FileHandler.loadWords("positive.txt");
        String[] negativeWords = FileHandler.loadWords("negative.txt");

        // read both text files
        String text1 = FileHandler.readFile(file1);
        String text2 = FileHandler.readFile(file2);

        // create objects for analysis for both text files
        LiteraryAnalysis analysis1 = new LiteraryAnalysis(text1, stopwords, positiveWords, negativeWords);
        LiteraryAnalysis analysis2 = new LiteraryAnalysis(text2, stopwords, positiveWords, negativeWords);

        // perform analysis for both text files
        analysis1.analyzeText();
        analysis2.analyzeText();

        // calculate similarity for text files
        double similarity = SimilarityCalculator.cosineSimilarity(analysis1, analysis2);

        // display results
        System.out.println();
        System.out.println("Similarity score: " + similarity);
        // use threshold of 0.7 to determine if texts are similar
        if (similarity >= 0.7)
        {
            System.out.println("The texts are similar."); // if more or equal to 0.7, then similar
        } else {
            System.out.println("The texts are different.");
        }

        // save report to two text files
        analysis1.saveReport("analysis_report_1.txt");
        analysis2.saveReport("analysis_report_2.txt");
        System.out.println("Analysis completed and reports saved.");

        scanner.close(); // close scanner
    }
}
