package code.shiming.com.glide471demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import code.shiming.com.imageloader471.GlideApp;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_click_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this,DemoActivity.class));
            }
        });
        findViewById(R.id.btn_click_me_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TransitionOptionsActivity.class));
            }
        });
        findViewById(R.id.btn_click_me_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ImageProgressActivity.class));
            }
        });
    }
}
