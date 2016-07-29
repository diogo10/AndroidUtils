package myapp.util;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

/**
 * Created by Diogo on 29/07/2016.
 */
public class MySingleton {

    private static MySingleton instance;
    private MySingleton(){}

    public static MySingleton getInstance(){

        if(instance == null)
        {
            initInstance();
        }
        return instance;
    }

    private static synchronized void initInstance()
    {
        if (instance == null)
        {
            instance = new MySingleton();
        }
    }

    public void showToast(String s) {
        Toast.makeText(MyApp.get(), s, Toast.LENGTH_LONG).show();
    }

    /**
     * Default view animation
     *
     * @param view - some button, image, etc.
     * @param anim - Ex: R.anim.fade_in
     */
    public void animView(View view, int anim){
        Animation animation = AnimationUtils.loadAnimation(MyApp.get(),anim);
        view.setAnimation(animation);
    }


}
