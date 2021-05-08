import CoordinatedTypes.CRectangle;
import CoordinatedTypes.СCoordinatedString;

import com.mifmif.common.regex.Generex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class CCompany implements IModel {

    private static final String ROLE_DEF = "- Role: ";
    private static final String NAME_DEF = "  Name: ";
    private static final String PHONE_DEF = "  Phone: ";
    private static final String EMAIL_DEF = "  Email: ";
    private static final String WEBSITE_DEF = "  Website: ";
    private static final String ADDRESS_DEF = "  Address:\n";
    private static final String NUMBERS_DEF = "  Numbers:\n";
    private static final String BANK_DEF = "  Bank:\n";
    private static final String EMPTY_STR = "";
    private static final String DELIMITER = ",";
    private static final String GB_PHONE_PATTERN = "((08|09) ([0-9]){3} ([0-9]{4}))"; // Шаблон номеров Англии
    private static final String URL = ".com";
    private static final String DOMAIN = "@gmail.com";

    private TCompanyRole role;
    private СCoordinatedString name = new СCoordinatedString();
    private СCoordinatedString phone = new СCoordinatedString();
    private СCoordinatedString email = new СCoordinatedString();
    private СCoordinatedString webSite = new СCoordinatedString();
    private CAddress address = new CAddress();
    private CIdNumbers idNumbers = new CIdNumbers();
    private CBank bank = new CBank();

    public CCompany( TCompanyRole r ) {
        role = r;
    }

    public СCoordinatedString GetName() { return name; }
    public СCoordinatedString GetPhone() { return phone; }
    public СCoordinatedString GetEmail() { return email; }
    public СCoordinatedString GetWebSite() { return webSite; }
    public CAddress GetAddress() { return address; }
    public CIdNumbers GetIdNumbers() { return idNumbers; }
    public CBank GetBank() { return bank; }
    public String GetRole() {
        switch( role ) {
            case CR_BU:
                return "Bisness Unit\n";
            case CR_Vendor:
                return "Vendor\n";
            case CR_ShipTo:
                return "Ship To\n";
            case CR_BillTo:
                return "Bill To\n";
            case CR_Shipper:
                return "Shipper\n";
            case CR_RemitTo:
                return "Remit To\n";
        }
        assert( false );
        return "";
    }

    @Override
    public void Generate() {
        try {
            // записи 7+ million-company-dataset с kaggle
            RandomAccessFile raf = new RandomAccessFile( "companies_sorted.csv", "r" );

            String companyInfo = EMPTY_STR;
            while( companyInfo.isEmpty() ) {
                try {
                    long nextBytePosition = ThreadLocalRandom.current().nextLong( raf.length() );
                    raf.seek( nextBytePosition );
                    raf.readLine();
                    companyInfo = raf.readLine().trim();
                } catch( IOException e ) {
                    System.out.println( "Failed, CCompany" );
                }
            }

            Scanner scanner = new Scanner( companyInfo );
            scanner.useDelimiter( DELIMITER );

            Integer index = 0;
            while( scanner.hasNext() ) {
                String info = scanner.next();
                if( !info.isEmpty() && info.charAt( 0 ) == '\"' ) {
                    while( !info.endsWith( "\"" ) ) {
                        info = info.concat( DELIMITER ).concat( scanner.next() );
                    }
                }
                info = info.replaceAll( "\"", "" );
                if( index.equals( 0 ) ) {
                    idNumbers.SetCID( info );
                } else if( index.equals( 1 ) ) {
                    name.Set( CStringsHelper.ToUpperCaseNames( info ) );
                } else if( index.equals( 2 ) ) {
                    webSite.Set( info );
                } else if( index.equals( 6 ) ) {
                    if( !info.isEmpty() ) {
                        String[] addressData = info.split( DELIMITER );
                        if( addressData.length > 0 ) {
                            address.SetCity( CStringsHelper.ToUpperCaseNames( addressData[0].substring( 1 ) ) ); // city
                        }
                        if( addressData.length > 1 ) {
                            address.SetState( CStringsHelper.ToUpperCaseNames( addressData[1].trim() ) ); // state
                        }
                        if( addressData.length > 2 ) {
                            address.SetCountry( CStringsHelper.ToUpperCaseNames( addressData[2].substring( 0, addressData[2].length() - 1 ) ) ); // country
                        }
                    }
                    address.Generate();

                    break;
                }
                index++;
            }

            raf.close();
        } catch( FileNotFoundException e ) {
            System.out.println( "Not fined csv file!" );
        } catch( IOException e ) {
            System.out.println( "Failed, CCompany" );
        }

        // TODO зависимость от языка...
        Generex phoneGenerex = new Generex( GB_PHONE_PATTERN );
        phone.Set( phoneGenerex.random() );

        email.Set( name.Get().length() < 6 ? name.Get().concat( DOMAIN ) :
                CStringsHelper.GetStartedOfStr( name.Get(), false ).concat( DOMAIN ) );

        if( webSite.GetValue().isEmpty() ) {
            webSite.Set( name.GetValue().replace( " ", "" ).concat( URL ) );
        }

        idNumbers.Generate();
        bank.Generate();
    }

    @Override
    public String GetData() {
        return ROLE_DEF.concat( GetRole() ).
                concat( name.Show( NAME_DEF ) ).
                concat( phone.Show( PHONE_DEF ) ).
                concat( email.Show( EMAIL_DEF ) ).
                concat( webSite.Show( WEBSITE_DEF ) ).
                concat( ADDRESS_DEF ).concat( address.GetData() ).
                concat( NUMBERS_DEF ).concat( idNumbers.GetData() ).
                concat( BANK_DEF ).concat( bank.GetData() );
    }

    @Override
    public void DrawRects( Graphics2D g2d ) {
        name.DrawRects( g2d );
        phone.DrawRects( g2d );
        email.DrawRects( g2d );
        webSite.DrawRects( g2d );
        address.DrawRects( g2d );
        idNumbers.DrawRects( g2d );
        bank.DrawRects( g2d );
    }
}
