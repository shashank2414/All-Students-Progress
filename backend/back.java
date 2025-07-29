class Tester {
    public static int power(int n, int p) 
    {
        int res = 1;
        for(int i=1; i<=p; i++)
        {
            res *= n; 
        }
        return res;
    }

    public static void main(String[] args) 
    {
        int num = 371;
        int temp = num;
        int count = 0;
        int ans = 0;
        while(temp > 0)
        {
            temp = temp/10; 
            count++;
        }
        temp = num;
        while(temp > 0)
        {
            int r = temp % 10;
            ans += power(r,count);
            temp = temp/10;
        }

        if(ans == num) 
        {
            System.out.println(num+" is an Armstrong number");
        }
        else
        {
            System.out.println(num + " is not an Armstrong number");
        }
    }
}