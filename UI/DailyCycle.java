package UI;

public class DailyCycle extends Thread{
    private NutriAppUI ui;

    public DailyCycle(NutriAppUI ui){
        this.ui = ui;
    }

    @Override
    public void run(){
        try {
            while(true){
                sleep(86400000);
                ui.dailyOperations();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
