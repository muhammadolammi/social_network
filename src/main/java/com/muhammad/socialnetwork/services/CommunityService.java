package com.muhammad.socialnetwork.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.muhammad.socialnetwork.model.Image;
import com.muhammad.socialnetwork.model.Message;
@Service
public class CommunityService {
   public List<Message> getCommunityMessages( int page){
      return Arrays.asList(new Message(1L, "First message"),
                new Message(2L, "Second message"));
    }

    public List<Image> getCommunityImmages( int page){
        return Arrays.asList(new Image("First Title", 1l, null), new Image("Second Title", 2L, null));

    }
   

    public Message postMessage( Message messageDto) {
        return new Message(3L, "New Content");
    }

    public Image postImage( MultipartFile file,String title) {
        return new Image("New Title", 3L, null);
        
    }
}
