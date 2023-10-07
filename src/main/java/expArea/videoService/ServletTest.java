package expArea.videoService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletTest extends HttpServlet{
	
	List<Video> videos = new ArrayList<>();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		
		resp.setContentType("text/plain");
		
		PrintWriter sendToClient = resp.getWriter();
		
		for(Video vid : videos) {
			sendToClient.write(vid.getName()+", "+vid.getUrl()+", "+String.valueOf(vid.getDuration())+"\n");
		}
		
		
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException  {
		String videoName = req.getParameter("name");
		String videoUrl = req.getParameter("url");
		String videoDurationStr = req.getParameter("duration");
		
		long videoDuration = -1;
		
		try {
			videoDuration = Long.parseLong(req.getParameter("duration"));			
		}
		catch(NumberFormatException e) {
			
		}
		
		resp.setContentType("text/plain");
		
		if(videoName != null || videoName.trim().length() > 1 || videoUrl != null ||
				videoUrl.trim().length() > 10 || videoDuration > 0 
				|| videoDurationStr.trim().length()>1) {
			Video vid = new Video(videoName, videoUrl, videoDuration);
			videos.add(vid);
		}
		else {
			resp.sendError(400, "request format is incorrect. missing name or url or duration");
		}
		
		resp.getWriter().write("Video Added");
		
	}
}
