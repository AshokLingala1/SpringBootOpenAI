package com.ashok.SpringAiWithOpenAI.controller;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.spi.AudioFileReader;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
public class AiController {
  
  // private OpenAiChatModel openAiChatModel;
  private OpenAiImageModel imageModel;
  private ChatClient chatClient;

  // public AiController(OpenAiChatModel openAiChatModel){
  //       this.chatClient =ChatClient.create(openAiChatModel);
  // }

  public AiController(OpenAiImageModel imageModel, ChatClient.Builder builder, OpenAiChatModel openAiChatModel) {
    this.imageModel = imageModel;
    this.chatClient = ChatClient.create(openAiChatModel);
  }


  // @GetMapping("/api/{prompt}")
  // public String generateResult(@PathVariable("prompt") String prompt){
  //   String content= chatClient.prompt(prompt).call().content();
  //   return content;
  // }

  @GetMapping("/api/{prompt}")
  public String generateImage(@PathVariable("prompt") String prompt){
    boolean image = prompt.contains("image");
    
    if(prompt.equals("who is your creator") || prompt.equals("who developed you")){
      return "Ashok Lingala ";
    }
    if(prompt.equals("who is Ashok")){
      return "He is My Developer 🥰 ";
    }



    if(image){
      ImagePrompt prompt1 = new ImagePrompt(prompt);
      ImageResponse response =imageModel.call(prompt1);
      String url = response.getResult().getOutput().getUrl();
      return url;
    }else{
        ChatResponse chatResponse= chatClient.prompt(prompt).call().chatResponse();

        // To Check Model of The AI, it is using
        System.out.println(chatResponse.getMetadata().getModel());
         AssistantMessage output= chatResponse.getResult().getOutput();
         return output.getText();


    }
    
  }


  @PostMapping("/info/{prompt}")
  public String describeImage(@PathVariable("prompt") String prompt, @RequestParam("file")MultipartFile file){

    System.out.println(file.getName());

    String content = chatClient.prompt().user(usr -> usr.text(prompt)
    .media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
    .call().content();

    return content;
  }
    

}

