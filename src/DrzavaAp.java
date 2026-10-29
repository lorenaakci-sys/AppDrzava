import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.sql.DataSource;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DrzavaAp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int odabir = 0;

        DataSource dataSource = createDataSource();

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("spojeno");
            while (odabir != 5) {
                System.out.println("1 - Nova država");
                System.out.println("2 - Izmjena postojeće države");
                System.out.println("3 - Brisanje postojeće države");
                System.out.println("4 - Prikaz država sortiranih po nazivu");
                System.out.println("5 - Kraj programa.");
                odabir = sc.nextInt();
                sc.nextLine();

                switch (odabir) {
                    case 1:
                        System.out.println("Dodajte državu: ");
                        String unos = sc.nextLine();
                        Statement stmt = connection.createStatement();
                        int rowsAffected = stmt.executeUpdate(
                                "INSERT INTO Drzava (Naziv) VALUES ('" + unos + "')"
                        );
                        System.out.println("Nova država " + unos + " je dodana.");
                        break;

                    case 2:
                        System.out.println("Unesite ID države: ");
                        int unos1 = sc.nextInt();
                        sc.nextLine();
                        Statement stmt2 = connection.createStatement();
                        if (unos1 > 3) {
                            System.out.println("Unesite novi naziv države: ");
                            String unos2 = sc.nextLine();
                            int rowsAffected2 = stmt2.executeUpdate(
                                    "UPDATE Drzava SET Naziv = ('" + unos2 + "') WHERE IDDrzava = " + unos1
                            );
                            System.out.println("Naziv države je promjenjen u " + unos2 + ".");
                        } else {
                            System.out.println("Ne možete mijenjati podatke ove države.");
                        }
                        break;

                    case 3:
                        System.out.println("Unesite ID države: ");
                        int unos3 = sc.nextInt();
                        sc.nextLine();
                        Statement stmt3 = connection.createStatement();
                        if (unos3 > 3) {
                            int rowsAffected3 = stmt3.executeUpdate("DELETE FROM Drzava WHERE IDDrzava = " + unos3);
                            System.out.println("Država je obrisana.");
                        } else {
                            System.out.println("Ne možete obrisati ovu državu.");
                        }
                        break;

                    case 4:
                        Statement stmt4 = connection.createStatement();
                        ResultSet rs = stmt4.executeQuery("SELECT IdDrzava, Naziv FROM Drzava ORDER BY Naziv ASC");
                        while (rs.next()) {
                            System.out.printf("%d %s\n", rs.getInt("idDrzava"), rs.getString("Naziv"));
                        }
                        break;

                    case 5:
                        System.out.println("Doviđenja.");
                        break;

                    default:
                        System.out.println("Neispravan unos.");
                        break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        private static DataSource createDataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("AdventureWorksOBP");
        dataSource.setUser("sa");
        dataSource.setPassword("sql");
        dataSource.setTrustServerCertificate(true);
        return dataSource;
    }
}
