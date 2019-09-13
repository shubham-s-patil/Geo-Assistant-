package io.github.yhdesai.dialogflow_example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;


public class MainActivity extends AppCompatActivity implements AIListener {


    private Button listenButton;
    private TextView resultTextView;
    private AIService aiService;

    // add your client access token here
    private String token = "0c54a2469e984c8ab4a5f934b0552d0c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = findViewById(R.id.listenButton);
        resultTextView = findViewById(R.id.resultTextView);
        final AIConfiguration config = new AIConfiguration(token,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            //Do something
            //aiService.startListening();
            Intent intent = new Intent(MainActivity.this, Edit_Agent.class);
            startActivity(intent);
        }
        return true;
    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }

    public void onResult(final AIResponse response) {
        Status status = response.getStatus();
        Result result = response.getResult();
        String action = result.getAction();
        int statusCode = status.getCode();
        String query = result.getResolvedQuery();
        final String speech = result.getFulfillment().getSpeech();
        final Metadata metadata = result.getMetadata();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.

        String resultText = "<p><font color=\"blue\">Query: </font></p>" +  query +
                "<p><font color=\"blue\">Status code: </font></p>" + statusCode +
                "<p><font color=\"blue\">Status type: </font></p>" + status.getErrorType() +
                "<p><font color=\"blue\">Action: </font></p>" + action +
                "<p><font color=\"blue\">Speech: </font></p>" + speech +
                "<p><font color=\"blue\">Metadata Intent ID: </font></p>" + metadata.getIntentId() +
                "<p><font color=\"blue\">Metadata Intent Name: </font></p>" + metadata.getIntentName() +
                "<p><font color=\"blue\">Parameters: </font></p>" + parameterString +
             //   "<p><font color=\"blue\">Metadata: </font></p>" + result.getMetadata() +
                "<p></p>";


        resultTextView.setText(Html.fromHtml(resultText), TextView.BufferType.SPANNABLE);



    }

    @Override
    public void onError(final AIError error) {

        String errorText = "<p><font color=\"blue\">Error: </font></p>" + error.toString();

        resultTextView.setText(Html.fromHtml(errorText), TextView.BufferType.SPANNABLE);

    }

    @Override
    public void onListeningStarted() {
        listenButton.setText("listening");
        listenButton.setTextColor(0xffffff00);
    }

    @Override
    public void onListeningCanceled() {
        listenButton.setText("listen");
        resultTextView.setText("Listening Cancelled");
        listenButton.setTextColor(0xffffffff);

    }

    @Override
    public void onListeningFinished() {
        listenButton.setText("listen");
        listenButton.setTextColor(0xffffffff);
    }

    @Override
    public void onAudioLevel(final float level) {
    }


}
