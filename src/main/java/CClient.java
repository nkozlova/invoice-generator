import javax.xml.ws.soap.Addressing;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
/*
public class CClient implements IModel {

    // Грузополучатель
    private String billingName;
    private CAddress billingAddress = new CAddress();
    // Плательщик
    private String shippingName;
    private CAddress shippingAddress = new CAddress();

    public String GetBillingName() { return billingName; }
    public CAddress GetBillingAddress() { return billingAddress; }
    public String GetShippingName() { return shippingName; }
    public CAddress GetShippingAddress() { return shippingAddress; }

    @Override
    public void Generate() {
        try {
            Random random = new Random();
            int skipedLines = random.nextInt(100);//7000000

            Scanner scanner = new Scanner(new File("companies_sorted.csv")); // записи 7+ million-company-dataset с kaggle

            for (int i = 0; i < skipedLines; i++) {
                scanner.nextLine();
            }

            String companyInfo = scanner.nextLine();

            Scanner scannerInfo = new Scanner(companyInfo);
            scannerInfo.useDelimiter(",");

            Integer index = 0;
            while (scannerInfo.hasNext()) {
                String info = scannerInfo.next();
                if (index.equals(1)) {
                    billingName = toUpperCaseNames(info);
                } else if (index.equals(6)) {
                    if (!info.isEmpty()) {
                        billingAddress.SetCity(toUpperCaseNames(info.substring(1))); // city
                        info = scannerInfo.next();     // state
                        if( "\"".equals(info.charAt(info.length() - 1))){
                            billingAddress.SetCountry(toUpperCaseNames(info.substring(0, info.length() - 1)));
                        } else {
                            info = scannerInfo.next().trim();  // country
                            billingAddress.SetCountry(toUpperCaseNames(info.substring(0, info.length() - 1)));
                        }
                        billingAddress.Generate();
                    } else {
                        billingAddress.Generate();
                    }
                    break;
                }
                index++;
            }


            skipedLines = random.nextInt(100); //7000000

            for (int i = 0; i < skipedLines; i++) {
                scanner.nextLine();
            }

            companyInfo = scanner.nextLine();

            scannerInfo = new Scanner(companyInfo);
            scannerInfo.useDelimiter(",");

            index = 0;
            while (scannerInfo.hasNext()) {
                String info = scannerInfo.next();
                if (index.equals(1)) {
                    shippingName = toUpperCaseNames(info);
                } else if (index.equals(6)) {
                    if (!info.isEmpty()) {
                        shippingAddress.SetCity(toUpperCaseNames(info.substring(1))); // city
                        info = scannerInfo.next();     // state
                        if( "\"".equals(info.charAt(info.length() - 1))){
                            shippingAddress.SetCountry(toUpperCaseNames(info.substring(0, info.length() - 1)));
                        } else {
                            info = scannerInfo.next().trim();  // country
                            shippingAddress.SetCountry(toUpperCaseNames(info.substring(0, info.length() - 1)));
                        }
                        shippingAddress.Generate();
                    } else {
                        shippingAddress.Generate();
                    }
                    break;
                }
                index++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("Not fined csv file!");
        }
    }

    @Override
    public void Show() {
        System.out.println("\tbillingName: " + billingName);
        System.out.println("\tbillingAddress { " );
        billingAddress.Show();
        System.out.println("\t}" );

        System.out.println("\tshippingName: " + shippingName);
        System.out.println("\tshippingAddress { " );
        shippingAddress.Show();
        System.out.println("\t}" );
    }

    // Приводим первые буквы названий к большому размеру
    private String toUpperCaseNames(String str) {
        String[] splits = str.split(" ");
        String res = "";
        for (String part: splits ) {
            res = res.concat(" ").concat(part.substring(0, 1).toUpperCase()).concat(part.substring(1));
        }
        return res.trim();
    }
}*/
