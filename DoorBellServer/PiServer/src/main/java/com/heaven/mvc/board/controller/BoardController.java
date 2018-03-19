package com.heaven.mvc.board.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.heaven.mvc.board.domain.BoardVO;
import com.heaven.mvc.board.service.BoardService;


@Controller
@SessionAttributes("boardVO")
public class BoardController {
	@Autowired
	private BoardService boardService;
	public BoardService getBoardService() {
		return boardService;
	}

	public void setBoardService(BoardService boardService) {
		this.boardService = boardService;
	}

	@RequestMapping(value = "/board/list")
	public String list(Model model) {
		model.addAttribute("boardList", boardService.list());
		return "/board/list";
	}

	@RequestMapping(value = "/board/read/{seq}")
	public String read(Model model, @PathVariable int seq) {
		model.addAttribute("boardVO", boardService.read(seq));
		return "/board/read";
	}

	@RequestMapping(value = "/board/write", method = RequestMethod.GET)
	public String write(Model model) {
		model.addAttribute("boardVO", new BoardVO());
		return "/board/write";
	}

	@RequestMapping(value = "/board/write", method = RequestMethod.POST)
	public String write(@Valid BoardVO boardVO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/board/write";
		} else {
			boardService.write(boardVO);
			return "redirect:/board/list";
		}
	}

	@RequestMapping(value = "/board/edit/{seq}", method = RequestMethod.GET)
	public String edit(@PathVariable int seq, Model model) {
		BoardVO boardVO = boardService.read(seq);
		model.addAttribute("boardVO", boardVO);
		return "/board/edit";
	}

	@RequestMapping(value = "/board/edit/{seq}", method = RequestMethod.POST)
	public String edit(@Valid @ModelAttribute BoardVO boardVO,
			BindingResult result, int pwd, SessionStatus sessionStatus,
			Model model) {
		if (result.hasErrors()) {
			return "/board/edit";
		} else {
			if (boardVO.getPassword() == pwd) {
				boardService.edit(boardVO);
				sessionStatus.setComplete();
				return "redirect:/board/list";
			}

			model.addAttribute("msg", "asdf");
			return "/board/edit";
		}
	}

	@RequestMapping(value = "/board/delete/{seq}", method = RequestMethod.GET)
	public String delete(@PathVariable int seq, Model model) {
		model.addAttribute("seq", seq);
		return "/board/delete";
	}

	@RequestMapping(value = "/board/delete", method = RequestMethod.POST)
	public String delete(int seq, int pwd, Model model) {
		int rowCount;
		BoardVO boardVO = new BoardVO();
		boardVO.setSeq(seq);
		boardVO.setPassword(pwd);

		rowCount = boardService.delete(boardVO);

		if (rowCount == 0) {
			model.addAttribute("seq", seq);
			model.addAttribute("msg", "asdf");
			return "/board/delete";
		} else {
			return "redirect:/board/list";
		}
	}

	@RequestMapping(value = "/test")
	@ResponseBody
	public String test(String name, int age) {
		return "<h1>" + name + age + "</h1>";
	}
	
	@RequestMapping(value="mobile/sendFCM")
	@ResponseBody //추가된 부분
    public String index(Model model, HttpServletRequest request, HttpSession session, @RequestBody String str)throws Exception{
            
				System.out.println(str); //추가된 부분
				//System.out.println(boardService.read("asdf").getId());
				System.out.println("-----------id---------");
                
                final String apiKey = "AAAA8cmMLE4:APA91bHhCQh1Am4BBHSdvoiaZKbs29ylOtOTv8aSCGsdt1aVP2M_S51oazheEGWeqB9rOpuNrVoaWrRRcCdjk2XdlaHrK2T9olkUvf48OBUkJ7D5EVMD8eVoHIAQVzAvVyRWg7ExOaG8";
                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "key=" + apiKey);
 
                conn.setDoOutput(true);
                
                String userId =(String) request.getSession().getAttribute("ssUserId");
 
                
                // 주제 ALL
                //String input = "{\"notification\" : {\"title\" : \"현관문 벨이 울렸습니다. \", \"body\" : \"카메라로 방문자를 확인하세요.\"}, \"to\":\"/topics/ALL\"}";
                // 주제 DoorBell
                String input = "{\"data\" : {\"title\" : \"현관문 벨이 울렸습니다. \", \"body\" : \"카메라로 방문자를 확인하세요.\", "
                		+ "\"sound\" : \"default\"}, \"to\":\"/topics/yoon\"}";
                // 특정 토큰
                //String input = "{\"data\" : {\"title\" : \" 여기다 제목넣기 \", \"body\" : \"여기다 내용 넣기\"}, \"to\":\"dFbxzkzV7QQ:APA91bGyeDYa35zY-ZXgBjD73tX5-SWbQfXgOHIj9Otb7lWef3XUtjF3urVkRl5dTVbM1Ea9QaRC-75i9Pm3pOpJkhpvtCHdduu0Fq19jZYd5-01K9gmNjGaTaAIcZRoTVNTlA8S-Lmv\"}";
 
                OutputStream os = conn.getOutputStream();
                
                // UTF-8로 인코딩해서 전송
                os.write(input.getBytes("UTF-8"));
                os.flush();
                os.close();
                
                int responseCode = conn.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + input);
                System.out.println("Response Code : " + responseCode);
                
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
                System.out.println(response.toString());
                
        return "/board/list";
    }
	

	
//	@RequestMapping(value = "/mobile/add", method=RequestMethod.POST, produces = {"application/json", "application/xml"})
//	public String post(@ModelAttribute("userVo") UserVO userVo, Model model) {
//		
//		
//		model.addAttribute("ip", userVo.getIp());
//		model.addAttribute("token", userVo.getToken());
//		model.addAttribute("a", "b");
//		System.out.println(userVo.getIp());
//		System.out.println("Just fine");
//		System.out.println(userVo.getToken());
//		System.out.println(model.toString());
//		return "/board/list";
//	}
	 //method=RequestMethod.POST, 
	
	@RequestMapping(value = "/mobile/add",produces = {"application/json", "application/xml"})
	@ResponseBody
	public String post(@RequestBody String str) {
		try {
			System.out.println(str);
			String strID, strIP, strPW, tempStr;
			int i = str.indexOf("&");
			strID = str.substring(3, i);
			tempStr = str.substring(i+1, str.length());
			i = tempStr.indexOf("&");
			strIP = tempStr.substring(3, i);
			tempStr = tempStr.substring(i+1, tempStr.length());
			strPW = tempStr.substring(3);
			System.out.println(strID);
			System.out.println(strIP);
			System.out.println(strPW);
			BoardVO boardVO = new BoardVO();
			
			boardVO.setId(strID);
			boardVO.setIp(strIP);
			boardVO.setPassword(Integer.parseInt(strPW));
			boardService.write(boardVO);
			
			String result = "ok";
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String result = "no";
			return result;
		}
		//return "/board/list";
	}
}

