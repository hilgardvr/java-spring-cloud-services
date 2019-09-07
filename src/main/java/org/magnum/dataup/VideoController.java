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
import org.magnum.dataup.model.VideoStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

@Controller
public class VideoController {

    private ArrayList<Video> videoList = new ArrayList<>();
    private static long videoId = 1;

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {

    }
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

	@RequestMapping("/")
    public void index(
            HttpServletResponse res
    ) {
	    res.setStatus(200);
    }

	@RequestMapping(value = "/video", method = RequestMethod.GET)
    @ResponseBody
	public ArrayList<Video> getVideos() {
	    return this.videoList;
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    @ResponseBody
    public Video postVideoMetadata(
            @RequestBody
            Video vid
    ) {
	    vid.setId(videoId);
        videoId++;
        vid.setDataUrl(getDataUrl(vid.getId()));
	    videoList.add(vid);
	    return vid;
    }

    @RequestMapping(value = "/video/{videoId}/data", method = RequestMethod.GET)
    @ResponseBody
    public void getVideo (
            @PathVariable
            long videoId,
            HttpServletResponse res
    ) {
	    for (Video vid : videoList) {
	        if (vid.getId() == videoId) {
	            try {
                    File file = new File("/tmp/video" + videoId);
                    if (!file.exists()) {
                        System.out.println("File doesn't exist");
                    }
                    InputStream in = new FileInputStream(file);
                    OutputStream out = res.getOutputStream();
                    byte[] bytes = new byte[in.available()];
                    in.read(bytes);
                    res.setStatus(200);
                    out.write(bytes);
                } catch (Exception ex) {
	                System.out.println("Failed to write file to http outputstream");
	                ex.printStackTrace();
                }
            }
        }
        System.out.println("Can't find ID " + videoId);
	    res.setStatus(404);
    }

    @RequestMapping(value = "/video/{videoId}/data", method = RequestMethod.POST)
    @ResponseBody
    public VideoStatus postVideoData(
            @RequestParam("data")
            MultipartFile videoData,
            @PathVariable
            long videoId
    ) {
        VideoStatus status;
	    for (Video vid : videoList) {
	        if (vid.getId() == videoId) {
	            try {
                    File file = new File("/tmp/video" + videoId);
                    InputStream in = videoData.getInputStream();
                    FileOutputStream out = new FileOutputStream(file);

                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = in.read(bytes)) !=  -1) {
                        out.write(bytes, 0, read);
                    }
                    System.out.println("Path: " + file.getAbsolutePath());
                    status = new VideoStatus(VideoStatus.VideoState.READY);
                } catch (Exception ex) {
                    System.out.println("Unable to save data");
                    ex.printStackTrace();
                    status = new VideoStatus(VideoStatus.VideoState.PROCESSING);
                }
                return status;
            }
        }
        throw new ResourceNotFoundException();
    }

    private String getDataUrl(long videoId){
        String url = getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
        return url;
    }

    private String getUrlBaseForLocalServer() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String base =
                "http://"+request.getServerName()
                        + ((request.getServerPort() != 80) ? ":"+request.getServerPort() : "");
        return base;
    }


}
