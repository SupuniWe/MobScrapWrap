package com.db.cdap.scrapwrap.chatbot;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.db.cdap.scrapwrap.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatbotMainActivity extends AppCompatActivity implements AIListener {

    RecyclerView chatbotMainRecyclerView;
    EditText chatbotMainEditxt;
    RelativeLayout chatbotMainbtnAdd;

    DatabaseReference chatbotDatabaseRef;
    FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> adapter;
    Boolean flagFab = true;

    ChatViewHolder holder;

    LinearLayoutManager linearLayoutManager;


    private AIService aiService;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot_main);

        toolbar = (Toolbar)findViewById(R.id.chatbot_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ScrapWrapBot");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chatbotMainRecyclerView = (RecyclerView)findViewById(R.id.chatbot_main_recyclerView);
        chatbotMainEditxt = (EditText)findViewById(R.id.chatbot_main_editTxt);
        chatbotMainbtnAdd = (RelativeLayout)findViewById(R.id.chatbot_main_btnAdd);

        final AIConfiguration aiConfiguration = new AIConfiguration("afb8ad5048a74e7f84639c8421ea9de2",
                                                    AIConfiguration.SupportedLanguages.English,
                                                    AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, aiConfiguration);
        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(aiConfiguration);

        final AIRequest aiRequest = new AIRequest();


        chatbotMainRecyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatbotMainRecyclerView.setLayoutManager(linearLayoutManager);

        chatbotDatabaseRef = FirebaseDatabase.getInstance().getReference();
        chatbotDatabaseRef.keepSynced(true);

        chatbotMainbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatbotMainEditxt.getText().toString().trim();

                if(!message.equals(""))
                {
                    ChatMessage chatMessage = new ChatMessage(message, "user");
                    chatbotDatabaseRef.child("Chatbot").push().setValue(chatMessage);

                    aiRequest.setQuery(message);
                    new AsyncTask<AIRequest, Void, AIResponse>(){

                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try{
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {

                            if(aiResponse!=null)
                            {
                                Result result = aiResponse.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                ChatMessage aiChatMsg = new ChatMessage(reply, "scrapwrapbot");

                                chatbotDatabaseRef.child("Chatbot").push().setValue(aiChatMsg);
                            }

                            //super.onPostExecute(aiResponse);
                        }
                    }.execute(aiRequest);

                    adapter.notifyDataSetChanged();


                }
                else {
                    aiService.startListening();
                }

                chatbotMainEditxt.setText("");
            }
        });
 }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<ChatMessage> chatOptions = new FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(chatbotDatabaseRef.child("Chatbot"), ChatMessage.class).build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(chatOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessage model) {

                if(model.getMsgUser().equals("user"))
                {
                    holder.txtRight.setText(model.getMsgText());
                    holder.txtRight.setVisibility(View.VISIBLE);
                    holder.txtLeft.setVisibility(View.GONE);
                }
                else
                {
                    holder.txtLeft.setText(model.getMsgText());
                    holder.txtRight.setVisibility(View.GONE);
                    holder.txtLeft.setVisibility(View.VISIBLE);
                }
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbot_message_list, parent, false);
                return new ChatViewHolder(view);
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);

                int messageCnt = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(lastVisiblePosition == -1 || (positionStart>= (messageCnt -1) && lastVisiblePosition == (positionStart -1)))
                {
                    chatbotMainRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        chatbotMainRecyclerView.setAdapter(adapter);

        adapter.startListening();


    }

    @Override
    public void onResult(AIResponse response) {
        Result result = response.getResult();

        Log.e("result", result.toString() );

        String message = result.getResolvedQuery();
        ChatMessage chat = new ChatMessage(message, "user");
        chatbotDatabaseRef.child("Chatbot").push().setValue(chat);

        //Log.e("Message", message );

        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMsg = new ChatMessage(reply, "scrapwrapbot");
        chatbotDatabaseRef.child("Chatbot").push().setValue(chatMsg);

        //Log.e("reply", reply );

       // adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
