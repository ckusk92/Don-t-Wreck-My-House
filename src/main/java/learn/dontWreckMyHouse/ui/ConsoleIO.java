package learn.dontWreckMyHouse.ui;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Component
public class ConsoleIO {

    private static final String INVALID_NUMBER
            = "[INVALID] Enter a valid number.";
    private static final String NUMBER_OUT_OF_RANGE
            = "[INVALID] Enter a number between %s and %s.";
    private static final String REQUIRED
            = "[INVALID] Value is required.";
    private static final String INVALID_DATE
            = "[INVALID] Enter a date in MM/dd/yyyy format.";

    private final Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    public String readString(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    public String readRequiredString(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (!result.isBlank()) {
                return result;
            }
            println(REQUIRED);
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readRequiredString(prompt));
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public double readDouble(String prompt, double min, double max) {
        while (true) {
            double result = readDouble(prompt);
            if (result >= min && result <= max) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readRequiredString(prompt));
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            int result = readInt(prompt);
            if (result >= min && result <= max) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public boolean readBoolean(String prompt) {
        while (true) {
            String input = readRequiredString(prompt).toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            println("[INVALID] Please enter 'y' or 'n'.");
        }
    }

    public LocalDate readLocalDate(String prompt) {
        while (true) {
            String input = readRequiredString(prompt);
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ex) {
                println(INVALID_DATE);
            }
        }
    }

    public LocalDate editLocalDate(String prompt) {
        while (true) {
            String input = readString(prompt);
            if(input.trim().length() == 0) {
                return LocalDate.of(1000,1,1);
            }
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException ex) {
                println(INVALID_DATE);
            }
        }
    }

    public BigDecimal readBigDecimal(String prompt) {
        while (true) {
            String input = readRequiredString(prompt);
            try {
                return new BigDecimal(input);
            } catch (NumberFormatException ex) {
                println(INVALID_NUMBER);
            }
        }
    }

    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        while (true) {
            BigDecimal result = readBigDecimal(prompt);
            // Need to verify compareTo() output
            if (result.compareTo(min) >= 0 && result.compareTo(max) <= 0) {
                return result;
            }
            println(String.format(NUMBER_OUT_OF_RANGE, min, max));
        }
    }

    public String readState(String prompt) {
        while (true) {
            String result = readString(prompt);
            if (result.isBlank()) {
                println(REQUIRED);
            } else if(result.length() != 2) {
                println("Please enter a 2 letter state abbreviation");
            } else  if (!result.equalsIgnoreCase("AL") &&      //1
                    !result.equalsIgnoreCase("AK") &&  //2
                    !result.equalsIgnoreCase("AZ") &&  //3
                    !result.equalsIgnoreCase("AR") &&  //4
                    !result.equalsIgnoreCase("CA") &&  //5
                    !result.equalsIgnoreCase("CO") &&  //6
                    !result.equalsIgnoreCase("CT") &&  //7
                    !result.equalsIgnoreCase("DE") &&  //8
                    !result.equalsIgnoreCase("FL") &&  //9
                    !result.equalsIgnoreCase("GA") &&  //10
                    !result.equalsIgnoreCase("HI") &&  //11
                    !result.equalsIgnoreCase("ID") &&  //12
                    !result.equalsIgnoreCase("IL") &&  //13
                    !result.equalsIgnoreCase("IN") &&  //14
                    !result.equalsIgnoreCase("IA") &&  //15
                    !result.equalsIgnoreCase("KS") &&  //16
                    !result.equalsIgnoreCase("KY") &&  //17
                    !result.equalsIgnoreCase("LA") &&  //18
                    !result.equalsIgnoreCase("ME") &&  //19
                    !result.equalsIgnoreCase("MD") &&  //20
                    !result.equalsIgnoreCase("MA") &&  //21
                    !result.equalsIgnoreCase("MI") &&  //22
                    !result.equalsIgnoreCase("MN") &&  //23
                    !result.equalsIgnoreCase("MS") &&  //24
                    !result.equalsIgnoreCase("MO") &&  //25
                    !result.equalsIgnoreCase("MT") &&  //26
                    !result.equalsIgnoreCase("NE") &&  //27
                    !result.equalsIgnoreCase("NV") &&  //28
                    !result.equalsIgnoreCase("NH") &&  //29
                    !result.equalsIgnoreCase("NJ") &&  //30
                    !result.equalsIgnoreCase("NM") &&  //31
                    !result.equalsIgnoreCase("NY") &&  //32
                    !result.equalsIgnoreCase("NC") &&  //33
                    !result.equalsIgnoreCase("ND") &&  //34
                    !result.equalsIgnoreCase("OH") &&  //35
                    !result.equalsIgnoreCase("OK") &&  //36
                    !result.equalsIgnoreCase("OR") &&  //37
                    !result.equalsIgnoreCase("PA") &&  //38
                    !result.equalsIgnoreCase("RI") &&  //39
                    !result.equalsIgnoreCase("SC") &&  //40
                    !result.equalsIgnoreCase("SD") &&  //41
                    !result.equalsIgnoreCase("TN") &&  //42
                    !result.equalsIgnoreCase("TX") &&  //43
                    !result.equalsIgnoreCase("UT") &&  //44
                    !result.equalsIgnoreCase("VT") &&  //45
                    !result.equalsIgnoreCase("VA") &&  //46
                    !result.equalsIgnoreCase("WA") &&  //47
                    !result.equalsIgnoreCase("WV") &&  //48
                    !result.equalsIgnoreCase("WI") &&  //49
                    !result.equalsIgnoreCase("WY") &&  //50
                    !result.equalsIgnoreCase("DC") &&
                    !result.equalsIgnoreCase("PR")) {
                println("Please enter a valid 2 letter state abbreviation");
                // Valid Case
            } else {
                return result;
            }
        }
    }
}
