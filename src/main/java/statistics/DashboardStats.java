package statistics;

import com.komeetta.dao.PurchaseOrderDAO;
import com.komeetta.dao.SalesOrderDAO;
import com.komeetta.model.LanguageUtil;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Date;

import java.util.ResourceBundle;

/**
 * Class for the dashboard statistics
 * Contains the total number of sales and purchase orders and the total revenue
 */
public class DashboardStats {
    private int totalSalesOrders;
    private int totalPurchaseOrders;
    private double totalRevenue;
    private double lastThreeMonthsRevenue;
    PurchaseOrderDAO pDao = new PurchaseOrderDAO();
    SalesOrderDAO sDao = new SalesOrderDAO();

    /**
     * Constructor for the DashboardStats class
     * Initializes the statistics
     */
    public DashboardStats() {
        setTotalRevenue();
        setTotalPurchaseOrders();
        setTotalSalesOrders();
        setLastThreeMonthsRevenue();
    }

    public int getTotalSalesOrders() {
        return totalSalesOrders;
    }

    public void setTotalSalesOrders() {
        totalSalesOrders = sDao.getNumberOfSalesOrders();

    }

    public int getTotalPurchaseOrders() {
        return totalPurchaseOrders;
    }

    public void setTotalPurchaseOrders() {
        totalPurchaseOrders = pDao.getNumberOfPurchaseOrders();
    }

    public void setTotalRevenue() {
        double totalPurchasePrice = pDao.getTotalPurchaseOrders();
        double totalSalesPrice = sDao.getTotalSaleOrders();
        totalRevenue = totalSalesPrice - totalPurchasePrice;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setLastThreeMonthsRevenue() {
        double totalPurchasePrice = pDao.getThreeMonthOrders();
        double totalSalesPrice = sDao.getThreeMonthOrders();
        lastThreeMonthsRevenue = totalSalesPrice - totalPurchasePrice;
    }

    public double getLastThreeMonthsRevenue() {
        return lastThreeMonthsRevenue;
    }

    public int getOrderDifference() {
        return totalSalesOrders - totalPurchaseOrders;
    }

    /**
     * Updates the statistics by calling the set methods
     */
    public void updateStats() {
        setTotalRevenue();
        setLastThreeMonthsRevenue();
        setTotalPurchaseOrders();
        setTotalSalesOrders();
    }

    /**
     * Creates a CSV file with the statistics
     * @param fileName the name of the file to be created (without the .csv extension)
     */
    public void createCvsStatsFile(String fileName) {
        updateStats(); // update stats before creating the file

        ResourceBundle bundle = ResourceBundle.getBundle("UIMessages", LanguageUtil.getCurrentLocale());

        // all files are created in the Reports folder (src/main/resources/Reports)
        String projectDir = System.getProperty("user.dir");
        String filePath = Paths.get(projectDir, "src", "main", "resources", "Statistics", fileName + ".csv").toString();

        File file = getUniqueFileName(filePath);

        try {
            file.createNewFile();
        } catch (Exception e) {
            System.out.println("Error creating file: " + file.getAbsolutePath());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {

            Date todayDate = new Date();
            String today = todayDate.toString();

            String[] date = {LanguageUtil.getString("str_date"), today};
            writer.writeNext(date);
            writer.writeNext(new String[]{}); // empty line

            String[] orderHeader = {
                    LanguageUtil.getString("str_label_total_sales_orders"),
                    LanguageUtil.getString("str_label_total_purchase_orders")
            };
            writer.writeNext(orderHeader);
            String[] orderData = {String.valueOf(totalSalesOrders), String.valueOf(totalPurchaseOrders)};
            writer.writeNext(orderData);
            writer.writeNext(new String[]{}); // empty line

            String[] revenueHeader = {
                    LanguageUtil.getString("str_label_total_revenue"),
                    LanguageUtil.getString("str_label_last_three_months_revenue")
            };
            writer.writeNext(revenueHeader);
            String[] revenueData = {String.valueOf(totalRevenue), String.valueOf(lastThreeMonthsRevenue)};
            writer.writeNext(revenueData);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Statistics file created: " + file.getAbsolutePath());
        }
    }

    /**
     * Returns a unique file name by adding a number to the end of the file name if the file already exists
     * @param fileName the name of the file
     * @return a file with unique name
     */
    public File getUniqueFileName(String fileName) {
        if (fileName.endsWith(".csv")) {
            fileName = fileName.substring(0, fileName.length() - 4); // remove the .csv extension
        }

        File file = new File(fileName + ".csv");

        int i = 1;
        while (file.exists()) { // if the file already exists, create a new file with the next number
            file = new File(fileName + i + ".csv");
            i++;
        }
        return file;
    }
}
