package hotel.service;

public class DbTest {
  public static void main(String[] args) {
    System.out.println("Db is loading from: " +
      Db.class.getProtectionDomain().getCodeSource().getLocation());

    try (var conn = Db.get()) {
      System.out.println("✅ Connected to MySQL successfully!");
    } catch (Exception e) {
      System.out.println("❌ Could not connect:");
      e.printStackTrace();
    }
  }
}

