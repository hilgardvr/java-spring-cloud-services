/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class VideoController {

    private ArrayList<Video> videoList = new ArrayList<>();
    private static int videoId = 1;
	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */
	@RequestMapping(value = "/video", method = RequestMethod.GET)
    @ResponseBody
	public ArrayList<Video> getVideos() {
	    //Video vid = new Video();
	    //vid.setTitle("test title");
	    //this.videoList.add(vid);
	    return this.videoList;
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    public void getVideoData(
        @RequestParam
        String title,
        @RequestParam
        long duration,
        @RequestParam
        String subject,
        @RequestParam
        String contentType
    ) {
	    Video newVid = Video.create()
                .withTitle(title)
                .withDuration(duration)
                .withSubject(subject)
                .withContentType(contentType)
                .build();
	    newVid.setId(videoId);
	    videoId++;
	    videoList.add(newVid);
    }

    @RequestMapping(value = "/video/{videoId}/data", method = RequestMethod.GET)
    public Video getVideo(
            @PathVariable
            long videoId
    ) {
	    for (Video vid : videoList) {
	        if (vid.getId() == videoId) {
	            return vid;
            }
        }
	    return null;
    }

    //@RequestMapping(value = "/video/{videoId}/data", method = RequestMethod.POST)
    //public V
}
