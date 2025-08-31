package AggregationA2;

class Author {
    //Implement your code here
    private String name;
    private String email;
    private char gender;
    
    public Author(String name,String email,char gender){
        this.name = name;
        this.email = email;
        this.gender = gender;
    }
    
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public char getGender(){
        return this.gender;
    }
    public void setGender(char gender){
        this.gender = gender;
    }
}


class Book {
    //Implement your code here 
    private String name;
    private Author author;
    private double price;
    private int quantity;
    
    public Book(String name,Author author,double price,int quantity){
        this.name = name;
        this.price = price;
        this.author = author;
        this.quantity = quantity;
    }
    
    public void displayAuthorDetails(){
        System.out.println("Displaying author details");
        System.out.println("Author name: "+author.getName());
        System.out.println("Author email: "+author.getEmail());
        System.out.println("Author gender: "+author.getGender());
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for author
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    // Getter and Setter for price
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getQuantity(){
        return quantity;
    }
    
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    
}


class Tester {
    public static void main(String[] args) {
        //Implement your code here
        Author author1 = new Author("Pamiela Crane","pamielacrane@gmail.com",'F');
        Book book1 = new Book("If Only She Knew",author1,252,12);
        book1.displayAuthorDetails();
    }
}


