
//SONAL KAMBLE

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PostFixExpression
{
    public static void main(String[] args)
    {
        Scanner sc;

        try{
            sc = new Scanner(new File("/Users/sonalkamble/Desktop/Sonal/Desktop/ADS - Kequin Li/assignment1/in.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Hello! This is a postfix expression calculator.");
        System.out.println();

        while(sc.hasNextLine())
        {
            String str = sc.nextLine();

            System.out.println();
            System.out.println("The expression to be evaluated is " + str);

            Integer result = token(str);

            if(result != 0)
                System.out.println("The value of this expression is " + result);
            System.out.println();

        }
        System.out.println("Bye-bye!");
    }

    public static Integer token(String str)
    {
        Scanner sc = new Scanner(System.in);

        Stack<Integer> operands = new Stack<>();

        Pattern pattern = Pattern.compile("(-?\\d+|[A-Za-z_][A-Za-z0-9_]*|<=|>=|==|!=|&&|\\|\\||\\+|-|\\*|/|_|!|#|\\^|<|>|\\$)");

        Matcher matcher = pattern.matcher(str);

        List<String> tokens = new ArrayList<>();
        Map<String, Integer> tokenValues = new HashMap<>();
        while (matcher.find())
            tokens.add(matcher.group());

        if(operands.empty())
        {
            for(String token : tokens)
            {
                switch(token)
                {
                    //case 1: binary operators
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                    case "^":
                    case "<":
                    case "<=":
                    case ">":
                    case ">=":
                    case "==":
                    case "!=":
                    case "&&":
                    case "||": if(!operands.empty())
                                operands.push(calc(token, operands.pop(), operands.pop()));
                        break;

                    //case 2 : unary operators
                    case "_":
                    case "!":
                    case "#": if(!operands.empty())
                                operands.push(calc(token, operands.pop()));
                        break;
                    case "$":
                        return operands.pop();

                    default: if(token.matches("[-+]?\\d+(\\.\\d+)?"))
                                operands.push(Integer.parseInt(token));
                             else if(token.matches("[A-Za-z_][A-Za-z0-9_]*"))
                             {
                                Integer value = 0;

                                if(!tokenValues.containsKey(token))
                                {
                                    System.out.print("Enter the value of " + token + " > ");
                                    value = sc.nextInt();
                                    tokenValues.put(token, value);
                                }
                                 operands.push(tokenValues.get(token));
                             }
                        break;
                }
            }
        }
        return operands.pop();
    }

    //Binary operations
    private static Integer calc(String token, Integer num2, Integer num1)
    {
        Integer result = 0;
            switch (token)
            {
                case "+": result = num2 + num1;
                    break;
                case "-": result = num1 - num2;
                    break;
                case "*": result = num2 * num1;
                    break;
                case "/": if(num2 != 0)
                              result = num1 / num2;
                          else
                              System.out.println("division by 0");
                    break;
                case "^": result = (int) Math.pow(num1,num2);
                    break;
                case "<": if(num1 < num2) result = 1; else result = 0;
                    break;
                case "<=": if(num1 <= num2) result = 1; else result = 0;
                    break;
                case ">": if(num1 > num2) result = 1; else result = 0;
                    break;
                case ">=": if(num1 >= num2) result = 1; else result = 0;
                    break;
                case "==": if(num1.equals(num2)) result = 1; else result = 0;
                    break;
                case "!=": if(num1.equals(num2)) result = 0; else result = 1;
                    break;
                case "&&": if((num1!= 0) && (num2!= 0)) result = 1; else result = 0;
                    break;
                case "||": if((num1!= 0) || (num2!= 0)) result = 1; else result = 0;
                    break;
            }
        return result;
    }

    //Unary operations
    private static Integer calc(String token, Integer num1)
    {
        Integer result = 0;
        switch (token)
        {
            case "_": result = num1 * -1;
                break;
            case "#": if(num1 > 0)
                        result = (int) Math.sqrt(num1);
                      else
                        System.out.println("sqrt of negative value");
                break;
            case "!": if(num1 > 0)
                        result = facto(num1);
                      else
                        System.out.println("factorial of negative value");

        }
        return result;
    }

    private static Integer facto(Integer num1)
    {
        if(num1 <= 1)
            return 1;
        else
            return num1 * facto(num1 - 1);
    }

}