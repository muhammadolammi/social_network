package com.muhammad.socialnetwork.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.muhammad.socialnetwork.dto.ImageDto;
import com.muhammad.socialnetwork.dto.MessageDto;
@Service
public class CommunityService {
   public List<MessageDto> getCommunityMessages( int page){
      return Arrays.asList(new MessageDto(1L, "First message"),
                new MessageDto(2L, "Second message"));
    }

    public List<ImageDto> getCommunityImmages( int page){
        return Arrays.asList(new ImageDto("First Title", 1l, null), new ImageDto("Second Title", 2L, null));

    }
   

    public MessageDto postMessage( MessageDto messageDto) {
        return new MessageDto(3L, "New Content");
    }

    public ImageDto postImage( MultipartFile file,String title) {
        return new ImageDto("New Title", 3L, null);
        
    }
}
