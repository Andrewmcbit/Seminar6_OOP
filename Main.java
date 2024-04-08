import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Main{
    public static void main(String[] args) {
        System.out.println("S");
        Order order = new Order(new EmailSender());
        order.createOrder();     
        OrderLogger orderLogger = new OrderLogger();
        orderLogger.orderLogging(order);

        System.out.println("O");
        Order discountOrder = new Customer(new EmailSender());
        discountOrder.createOrder();
        orderLogger.orderLogging(discountOrder);

        System.out.println("L");
        Rectangle rect = new Rectangle();
        System.out.println("Rectangle perimeter = " + rect.calculatePerimeter(10.0, 5.5));
        Rectangle sq = new Square();
        System.out.println("Square perimeter = " + sq.calculatePerimeter(10.0, 5.5));

        System.out.println("I");
        Car car = new Car();
        car.drive();
        
        Airplane airplane = new Airplane();
        airplane.drive();
        airplane.fly();

        System.out.println("D");
        Logger logger = new Logger(new FileLogger(), new DatabaseLogger());
        logger.dbLog();
        logger.fileLog();

    }
}

class Order {
    private NotificatiorSender ns;
    
    public Order(NotificatiorSender ns){
        this.ns = ns;
    }
    public void createOrder(){
        System.out.println("Order created");
        ns.sendNotification();
    }
}

interface NotificatiorSender{
    public void sendNotification();
}

class EmailSender implements NotificatiorSender{
    @Override
    public void sendNotification(){
        System.out.println("Notification send");
    }
}
// S - Принцип единственной ответственности (Single Responsibility Principle):
// Добавьте новый класс OrderLogger, который будет отвечать только за логирование информации 
// о заказах. Теперь класс Order будет отвечать только за размещение заказа, а 
// OrderLogger - за запись логов.

class OrderLogger{
    List<Order> orderList;
    public OrderLogger(){
        orderList = new ArrayList<>();
    }
    public void orderLogging(Order order){
        System.out.println("Order logged");
        orderList.add(order);
    }
}


// O - Принцип открытости/закрытости (Open/Closed Principle):
// Добавьте новый метод calculateDiscount() в класс Customer, который будет рассчитывать 
// скидку для заказов. Это позволит расширить функциональность класса без изменения его
//  исходного кода.

class Customer extends Order{
    public Customer(NotificatiorSender ns) {
        super(ns);
    }

    @Override
    public void createOrder(){
        super.createOrder();
        System.out.printf("Your discount = %d \n",calculateDiscount());
    }

    public int calculateDiscount(){
        Random rnd = new Random();
        return rnd.nextInt(10);
    }
}

// L - Принцип подстановки Барбары Лисков (Liskov Substitution Principle):
// Создайте класс Square, который наследуется от класса Rectangle. Убедитесь, 
// что все методы, работающие с Rectangle, также корректно работают с Square, 
// не изменяя их поведения.

class Rectangle{
    public double calculatePerimeter(double a, double b){
        return (a + b) * 2;
    }
}

class Square extends Rectangle{
    @Override
    public double calculatePerimeter(double a, double b){
        return 4*a;
    }
}

// I - Принцип разделения интерфейса (Interface Segregation Principle):
// Создайте интерфейсы Driveable и Flyable. Реализуйте их в соответствующих классах 
// Car и Airplane. Теперь классы Car и Airplane зависят только от интерфейсов, которые 
// они используют.


interface Driveable {
    void drive();
}

interface Flyable{
    void fly();
}

class Car implements Driveable{
    // машина не умеет летать, потоэтму нет смысла имплементировать интерфейс Flyable в класс Car
    @Override
    public void drive(){
        System.out.println("The car is driving");
    }
}

class Airplane implements Driveable, Flyable{
    @Override
    public void drive(){
        System.out.println("The plane is driving");
    }
    @Override
    public void fly(){
        System.out.println("The plane is flying");
    }
}

// D - Принцип инверсии зависимостей (Dependency Inversion Principle):
// Создайте абстракцию Logger, которая будет иметь методы для логирования различных 
// типов сообщений. Затем создайте классы FileLogger и DatabaseLogger, реализующие 
// эту абстракцию. Теперь классы, которые требуют логирования, будут зависеть 
// только от Logger, а не от конкретных реализаций.

interface LogInFile{
    void fileLogger();
}
interface LogInDB{
    void dbLogger();
}

class FileLogger implements LogInFile{
    @Override
    public void fileLogger() {
        System.out.println("Log in file");
    }

}

class DatabaseLogger implements LogInDB{
    @Override
    public void dbLogger() {
        System.out.println("Log in db");
    }

}

class Logger{
    LogInFile logf;
    LogInDB logdb;

    public Logger(LogInFile lf, LogInDB ldb){
        this.logf = lf;
        this.logdb = ldb;
    }

    public void dbLog(){
        logdb.dbLogger();
    }
    public void fileLog(){
        logf.fileLogger();
    }
}