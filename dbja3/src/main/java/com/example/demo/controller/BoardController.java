package com.example.demo.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.BoardDAO_jpa;
import com.example.demo.dao.CommentsDAO_jpa;
import com.example.demo.dao.EventDAO_jpa;
import com.example.demo.dao.LikeBoardDAO_jpa;
import com.example.demo.entity.Board;
import com.example.demo.entity.Comments;
import com.example.demo.entity.Event;
import com.example.demo.entity.Likeboard;
import com.example.demo.entity.Member;
import com.example.demo.entity.Reviewcomment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import java.io.File;

import com.example.demo.dao.ReviewBoardDAO_jpa;
import com.example.demo.dao.ReviewCommentDAO_jpa;
import com.example.demo.entity.Reviewboard;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Setter
public class BoardController {

	public int pageSIZE = 25;
	public int totalRecord;
	public int totalPage;
	
	@Autowired
	private BoardDAO_jpa boarddao_jpa;
	@Autowired
	private EventDAO_jpa eventdao_jpa;
	@Autowired
	private ReviewBoardDAO_jpa reviewboarddao_jpa;
	@Autowired
	private CommentsDAO_jpa commentsdao_jpa;
	@Autowired
	private ReviewCommentDAO_jpa reveiwcommentdao_jpa;
	@Autowired
	private LikeBoardDAO_jpa likeboarddao_jpa;
	
	//-----------------------후기 게시판
	 //댓글 수정하기
    @GetMapping("/reviewCommentUpdate")
    @ResponseBody
    public void reviewCommentUpdate(int rcomno,String rcomcontent) {
    	reveiwcommentdao_jpa.updateByRcomno(rcomno, rcomcontent);
    }
    
    //댓글 삭제하기
    @GetMapping("/reviewCommentDelete")
    @ResponseBody
    public void reviewCommentDelete(@RequestParam int rcomno) {
    	reveiwcommentdao_jpa.deleteById(rcomno);
    }
    
    //댓글 추가하기
    @PostMapping("/reviewInsertComment")
    public ModelAndView reviewInsertComment(Reviewcomment c,int reviewno,HttpSession session) {
    	//게시글 번호
    	Reviewboard rb=new Reviewboard();
    	rb.setReviewno(reviewno);
    	c.setReviewboard(rb);
    	
    	//작성자 
        String id = ((Member)session.getAttribute("m")).getId();
        Member member=new Member();
        member.setId(id);
        c.setMember(member);
        
        //작성일
        c.setRegdate(new Date());
    	
        //댓글 번호
        c.setRcomno(reveiwcommentdao_jpa.findByNext());
        
        //댓글 저장
        reveiwcommentdao_jpa.save(c);
        
        System.out.println("c:"+c);
        
    	ModelAndView mav=new ModelAndView("redirect:/boards/review/reviewDetail?reviewno="+reviewno);
    	
    	
    	return mav;
    }
	//조회수 추가
	@GetMapping("/updateHit")
	@ResponseBody
	public void updateHit(int hit,int reviewno) {
		int re=reviewboarddao_jpa.updateHit(hit, reviewno);
	}
	
	//좋아요 개수 추가
	@GetMapping("/updateReviewLike")
	@ResponseBody
	public int plusLike(HttpSession session, int like,int reviewno) {
		String id = ((Member)session.getAttribute("m")).getId();
		
		// 만약 해당 id를 가진 사람이 좋아요를 누르지 않았다면
		if(likeboarddao_jpa.countByIdAndReviewno(id, reviewno) == 0) {
			Likeboard likeboard = new Likeboard();
			
			// likeboard 객체 만들어 db에 추가
			likeboard.setLikeno(likeboarddao_jpa.getLikeno());
			likeboard.setMemberId(id);
			likeboard.setBoardno(reviewno);
			likeboarddao_jpa.save(likeboard);
			
			// 좋아요 +1 
			boarddao_jpa.updatefreeLike(like+1, reviewno);
			
			// 반환값 1
			return 1;
		}
		
		// 해당 id를 가진 사람이 좋아요를 눌렀다면
		else {
			
			// likeboard db에서 삭제
			likeboarddao_jpa.deleteByIdAndBoardno(id, reviewno);
			// 좋아요 -1
			boarddao_jpa.updatefreeLike(like-1, reviewno);
			// 반환값 2
			return 2;
		}		
	}

	
	//게시글 삭제
	@GetMapping("/deleteReview")
	@ResponseBody
	public void deleteReview(@RequestParam int reviewno) {
		int re=reviewboarddao_jpa.deleteByNo(reviewno);
	}
	
	//게시물 수정 페이지 이동
	@GetMapping("/boards/review/reviewUpdate")
	public void reviewupdate(HttpSession session, @RequestParam int reviewno,Model model) {
		Reviewboard review=new Reviewboard();
		review=reviewboarddao_jpa.findByNo(reviewno);
		//후기 게시물 행사번호
		int eventno=review.getEventno();  
		//게시물 내용 가져오기
		model.addAttribute("r", review);
		//아이디
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("id", id);
		//공연정보
		Event event=new Event();
		event= eventdao_jpa.findByEventno(eventno);
		model.addAttribute("event", event);
		
		Date today=new Date();
		model.addAttribute("today", today);
		//행사리스트
		model.addAttribute("list", eventdao_jpa.findAll());
	}
	//후기 게시글 수정
	@PostMapping("/boardUpdate")
	public ModelAndView boardUpdate(HttpSession session, Reviewboard r,String Contents) {
		ModelAndView mav=new ModelAndView("redirect:/boards/review/reviewlist");
		//게시물 작성하기
		// Member 객체 생성 및 설정
		String id = ((Member)session.getAttribute("m")).getId();
		Member member = new Member();
		member.setId(id);
		// Reviewboard 객체에 Member 객체 설정
		r.setMember(member);
		r.setRegdate(new Date());
		r.setReviewcontent(Contents);
		reviewboarddao_jpa.save(r);
		return mav;
	}
	//게시물 상세 페이지 이동
	@GetMapping("/boards/review/reviewDetail")
	public String reviewDetail(HttpSession session, @RequestParam int reviewno,Model model) {
		Reviewboard review=new Reviewboard();
		review=reviewboarddao_jpa.findByNo(reviewno);
		//후기 게시물 행사번호
		int eventno=review.getEventno();  
		//게시물 내용 가져오기
		model.addAttribute("r", review);
		//아이디
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("id", id);
		//공연정보
		Event event=new Event();
		event= eventdao_jpa.findByEventno(eventno);
		model.addAttribute("event", event);
		
		//게시물 댓글 가져오기
		model.addAttribute("com", reveiwcommentdao_jpa.findByReviewBoardNo(reviewno));
		
		// 좋아요 여부 가져오기
		model.addAttribute("likeboard", likeboarddao_jpa.countByIdAndReviewno(id, reviewno));
		
		String start=event.getEventstart().toString();
		String end=event.getEventend().toString();
		String day=start.substring(0, start.indexOf(" "))+" ~ "+end.substring(0, end.indexOf(" "));
		model.addAttribute("day", day);
		return "/boards/review/reviewDetail";
	}
	
	// 리뷰 게시판 검색/페이징/정렬
	@GetMapping(value={"/boards/review/reviewlist", "/boards/review/reviewlist", "/boards/review/reviewlist/{page}", 
			"/boards/review/reviewlist/{keyword}/{page}", "/boards/review/reviewlist/{keyword}/{page}/{orderby}"})
	public ModelAndView reviewlist(HttpSession session, @PathVariable(required=false) String keyword, 
			@PathVariable(required=false) String orderby, HttpServletRequest request, @PathVariable(required = false) Integer page) {
		ModelAndView mav = new ModelAndView("/boards/review/reviewlist");

		if(orderby == null) {
			if(session.getAttribute("orderby") != null && !session.getAttribute("orderby").equals("")) {
				orderby = (String) session.getAttribute("orderby");
			}
			else {
				orderby="regdate";
			}	
		}
		String key = "all";
		if(page == null) {
			page = 1;
		}
		if(keyword == null) {
			key = "all";
		}

		Page<Reviewboard> list;
		
		if(session.getAttribute("keyword")!=null) {
			key = (String)session.getAttribute("keyword");
		}
		if(keyword != null) {
			key = keyword;
		}
		
		Pageable pageable;
		
		if(key.equals("all")) {
			if(orderby.equals("regdate")) {
				pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("reviewhit").descending());
			}
			list = reviewboarddao_jpa.findByBcategory("", pageable);
		}
		else {
			if(orderby.equals("regdate")) {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("reviewhit").descending());
			}
			list = reviewboarddao_jpa.findByBcategory(keyword, pageable);

		}
		
	    List<List<Reviewboard>> rows = new ArrayList<>();
	    List<Reviewboard> boardlist = new ArrayList<Reviewboard>();
	    List<Reviewboard> currentRow = null;
		session.setAttribute("keyword", key);
		session.setAttribute("orderby", orderby);
		
	    for (Reviewboard board : list.getContent()) {
	    	
	    	boardlist.add(board);
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(board);
	    }
		
		mav.addObject("list", boardlist);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", list.getTotalPages());

		return mav;
	}
	
	//후기 게시글 작성 페이지
	@GetMapping("/boards/review/insertBoard_review")
	public void reivew(HttpSession session, Model model) {
		model.addAttribute("list", eventdao_jpa.findAll());
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("id", id);
	}
	
	//후기 게시글 작성
	@PostMapping("/board")
	public ModelAndView board(HttpSession session, Reviewboard r,String Contents) {
		ModelAndView mav=new ModelAndView("redirect:/boards/review/reviewlist");
		//게시물 작성하기
		r.setReviewno(reviewboarddao_jpa.findNextNo());	
		// Member 객체 생성 및 설정
		String id = ((Member)session.getAttribute("m")).getId();
		Member member = new Member();
		member.setId(id);
		// Reviewboard 객체에 Member 객체 설정
		r.setMember(member);
		r.setReviewlike(0);
		r.setRegdate(new Date());
		r.setReviewhit(1);
		r.setReviewcontent(Contents);
		reviewboarddao_jpa.save(r);
		return mav;
	}

	
	//후기 게시글 작성 시 사진이 있으면 폴더에 저장
	@PostMapping(value="/uploadSummernoteImageFile")
	@ResponseBody
	public String uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {
		JsonObject jsonObject = new JsonObject();
		
		String fileRoot = "C:\\summer\\";	//저장될 외부 파일 경로
		String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
				
		String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명
		File targetFile = new File(fileRoot + savedFileName);	
		System.out.println(targetFile);
		HashMap<String, String> jsonResponse = new HashMap<>();
		
		try {
			InputStream fileStream = multipartFile.getInputStream();
			FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
			jsonResponse.put("url", "/summernoteImage/" + savedFileName);
			jsonResponse.put("responseCode", "success");

		} catch (IOException e) {
			FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
			jsonResponse.put("responseCode", "error");
			e.printStackTrace();
		}
		 // Gson 객체를 사용하여 HashMap을 JSON 문자열로 변환
	    Gson gson = new Gson();
	    String jsonString = gson.toJson(jsonResponse);
	    
		
		return jsonString;
	}
	//-----------------------
	//-----------------------자유게시판

	//게시글 삭제
	@GetMapping("/deletefree")
	@ResponseBody
	public void deletefree(@RequestParam int boardno) {
		int re=boarddao_jpa.deleteByNo(boardno);
	}
	
	//자유 게시글 수정
	@PostMapping("/freeUpdate")
	public ModelAndView freeUpdate(HttpSession session, Board b,String Contents) {
		ModelAndView mav=new ModelAndView("redirect:/boards/board/freelist");
		//게시물 작성하기
		// Member 객체 생성 및 설정
		String id = ((Member)session.getAttribute("m")).getId();
		Member member = new Member();
		member.setId(id);
		b.setMember(member);
		b.setRegdate(new Date());
		b.setBoardcontent(Contents);
		b.setBcategory("자유");
		boarddao_jpa.save(b);
		return mav;
	}
	
	//게시물 수정 페이지 이동
	@GetMapping("/boards/board/updateFree")
	public void boardupdate(HttpSession session, @RequestParam int boardno,Model model) {
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("b", boarddao_jpa.findByNo(boardno));
		model.addAttribute("id", id);
	}
	//조회수 추가
	@GetMapping("/updatefreeHit")
	@ResponseBody
	public void updatefreHit(int hit,int boardno) {
		int re=boarddao_jpa.updateHit(hit, boardno);
	}
	
	//좋아요 개수 추가
	@GetMapping("/updatefreeLike")
	@ResponseBody
	public int updatefreeLike(HttpSession session, int like,int boardno) {
		String id = ((Member)session.getAttribute("m")).getId();
		
		// 만약 해당 id를 가진 사람이 좋아요를 누르지 않았다면
		if(likeboarddao_jpa.countByIdAndBoardno(id, boardno) == 0) {
			Likeboard likeboard = new Likeboard();
			
			// likeboard 객체 만들어 db에 추가
			likeboard.setLikeno(likeboarddao_jpa.getLikeno());
			likeboard.setMemberId(id);
			likeboard.setBoardno(boardno);
			likeboarddao_jpa.save(likeboard);
			
			// 좋아요 +1 
			boarddao_jpa.updatefreeLike(like+1, boardno);
			
			// 반환값 1
			return 1;
		}
		
		// 해당 id를 가진 사람이 좋아요를 눌렀다면
		else {
			
			// likeboard db에서 삭제
			likeboarddao_jpa.deleteByIdAndBoardno(id, boardno);
			// 좋아요 -1
			boarddao_jpa.updatefreeLike(like-1, boardno);
			// 반환값 2
			return 2;
		}		
	}
	
	//게시물 상세 페이지 이동
	@GetMapping("/boards/board/freeDetail")
	public String freeDetail(HttpSession session, @RequestParam int boardno,Model model) {
		Board board=new Board();
		board=boarddao_jpa.findByNo(boardno);
		//게시물 내용 가져오기
		model.addAttribute("b", board);
		//아이디
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("id", id);
		
		//게시물 댓글 가져오기
		model.addAttribute("com", commentsdao_jpa.findByBoardNo(boardno));

		// 좋아요 여부 가져오기
		model.addAttribute("likeboard", likeboarddao_jpa.countByIdAndBoardno(id, boardno));
		
		return "/boards/board/freeDetail";
	}
	//게시글 작성 페이지
	@GetMapping("/boards/board/insertBoard_free")
	public void free(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("id", id);
	}
	//게시글 저장
	@PostMapping("/free")
	public ModelAndView free(HttpSession session, Board b,String Contents) {
		ModelAndView mav=new ModelAndView("redirect:/boards/board/freelist");
		b.setBoardno(boarddao_jpa.findNextNo());
		String id = ((Member)session.getAttribute("m")).getId();
		Member member = new Member();
		member.setId(id);
		b.setMember(member);
		b.setBoardlikes(0);
		b.setRegdate(new Date());
		b.setBoardhit(1);
		b.setBoardcontent(Contents);
		boarddao_jpa.save(b);
		return mav;
	}
	
	//게시글 list
	@GetMapping(value={"/boards/board/freelist", "/boards/board/freelist/", "/boards/board/freelist/{page}", 
			"/boards/board/freelist/{keyword}/{page}", "/boards/board/freelist/{keyword}/{page}/{orderby}"})
	public ModelAndView freelist(HttpSession session, @PathVariable(required=false) String keyword, 
			@PathVariable(required=false) String orderby, HttpServletRequest request, @PathVariable(required = false) Integer page) {
		ModelAndView mav = new ModelAndView("/boards/board/freelist");

		if(orderby == null) {
			if(session.getAttribute("orderby") != null && !session.getAttribute("orderby").equals("")) {
				orderby = (String) session.getAttribute("orderby");
			}
			else {
				orderby="regdate";
			}	
		}
		String key = "all";
		if(page == null) {
			page = 1;
		}
		if(keyword == null) {
			key = "all";
		}

		Page<Board> list;
		
		if(session.getAttribute("keyword")!=null) {
			key = (String)session.getAttribute("keyword");
		}
		if(keyword != null) {
			key = keyword;
		}
		
		Pageable pageable;
		
		if(key.equals("all")) {
			if(orderby.equals("regdate")) {
				pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("boardhit").descending());
			}
			list = boarddao_jpa.findByBcategory("자유", "", pageable);
		}
		else {
			if(orderby.equals("regdate")) {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("boardhit").descending());
			}
			list = boarddao_jpa.findByBcategory("자유", keyword, pageable);

		}
		
	    List<List<Board>> rows = new ArrayList<>();
	    List<Board> boardlist = new ArrayList<Board>();
	    List<Board> currentRow = null;
		session.setAttribute("keyword", key);
		session.setAttribute("orderby", orderby);
		
	    for (Board board : list.getContent()) {
	    	
	    	boardlist.add(board);
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(board);
	    }
	    
		mav.addObject("list", boardlist);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", list.getTotalPages());

		return mav;
	}
	  //댓글 수정하기
    @GetMapping("/freeCommentUpdate")
    @ResponseBody
    public void freeCommentUpdate(int comno,String comcontent) {
    	commentsdao_jpa.updateByComno(comno, comcontent);
    }
    
    //댓글 삭제하기
    @GetMapping("/freeCommentDelete")
    @ResponseBody
    public void freeCommentDelete(@RequestParam int comno) {
    	commentsdao_jpa.deleteById(comno);
    }
    
    //댓글 추가하기
    @PostMapping("/freeInsertComment")
    public ModelAndView freeInsertComment(Comments c,int boardno,HttpSession session) {
    	//게시글 번호
    	Board board=new Board();
    	board.setBoardno(boardno);
    	c.setBoard(board);
    	
    	//작성자 
        String id = ((Member)session.getAttribute("m")).getId();
        Member member=new Member();
        member.setId(id);
        c.setMember(member);
        
        //작성일
        c.setRegdate(new Date());
    	
        //댓글 번호
        c.setComno(commentsdao_jpa.findByNext());
        
        //댓글 저장
        commentsdao_jpa.save(c);
        
    	ModelAndView mav=new ModelAndView("redirect:/boards/board/freeDetail?boardno="+boardno);
    	
    	
    	return mav;
    }
	
	//--------------------------------------
	//-----------------동행게시판
	//게시글 작성 페이지
	@GetMapping("/boards/board/insertBoard_together")
	public void together(HttpSession session, Model model) {
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("id", id);
	}
	//게시글 저장
	@PostMapping("/together")
	public ModelAndView together(HttpSession session, Board b,String Contents) {
		ModelAndView mav=new ModelAndView("redirect:/boards/board/togetherlist");
		b.setBoardno(boarddao_jpa.findNextNo());
		String id = ((Member)session.getAttribute("m")).getId();
		Member member = new Member();
		member.setId(id);
		b.setMember(member);
		b.setBoardlikes(0);
		b.setRegdate(new Date());
		b.setBoardhit(1);
		b.setBoardcontent(Contents);
		boarddao_jpa.save(b);
		return mav;
	}
	//동행 게시글 수정
	@PostMapping("/togetherUpdate")
	public ModelAndView togetherUpdate(HttpSession session, Board b,String Contents) {
		ModelAndView mav=new ModelAndView("redirect:/boards/board/togetherlist");
		//게시물 작성하기
		// Member 객체 생성 및 설정
		String id = ((Member)session.getAttribute("m")).getId();
		Member member = new Member();
		member.setId(id);
		b.setMember(member);
		b.setRegdate(new Date());
		b.setBoardcontent(Contents);
		b.setBcategory("동행");
		boarddao_jpa.save(b);
		return mav;
	}
	//게시글 삭제
	@GetMapping("/deleteTogether")
	@ResponseBody
	public void deleteTogether(@RequestParam int boardno) {
		int re=boarddao_jpa.deleteByNo(boardno);
	}
	
	//게시물 수정 페이지 이동
	@GetMapping("/boards/board/updateTogether")
	public void updateTogether(HttpSession session, @RequestParam int boardno,Model model) {
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("b", boarddao_jpa.findByNo(boardno));
	}
		
	//게시물 상세 페이지 이동
	@GetMapping("/boards/board/togetherDetail")
	public String togetherDetail(HttpSession session, @RequestParam int boardno,Model model) {
		Board board=new Board();
		board=boarddao_jpa.findByNo(boardno);
		//게시물 내용 가져오기
		
		model.addAttribute("b", board);
		//아이디
		String id = ((Member)session.getAttribute("m")).getId();
		model.addAttribute("id", id);
		
		//게시물 댓글 가져오기
		model.addAttribute("com", commentsdao_jpa.findByBoardNo(boardno));
		// 좋아요 여부 가져오기
		model.addAttribute("likeboard", likeboarddao_jpa.countByIdAndBoardno(id, boardno));
		
		return "/boards/board/togetherDetail";
	}
	//게시글 list
	@GetMapping(value={"/boards/board/togetherlist", "/boards/board/togetherlist/", "/boards/board/togetherlist/{page}", 
			"/boards/board/togetherlist/{keyword}/{page}", "/boards/board/togetherlist/{keyword}/{page}/{orderby}"})
	public ModelAndView togetherlist(HttpSession session, @PathVariable(required=false) String keyword,
			@PathVariable(required=false) String orderby, HttpServletRequest request, @PathVariable(required = false) Integer page) {
		ModelAndView mav = new ModelAndView("/boards/board/togetherlist");

		if(orderby == null) {
			if(session.getAttribute("orderby") != null && !session.getAttribute("orderby").equals("")) {
				orderby = (String) session.getAttribute("orderby");
			}
			else {
				orderby="regdate";
			}	
		}
		String key = "all";
		if(page == null) {
			page = 1;
		}
		if(keyword == null) {
			key = "all";
		}

		Page<Board> list;
		
		if(session.getAttribute("keyword")!=null) {
			key = (String)session.getAttribute("keyword");
		}
		if(keyword != null) {
			key = keyword;
		}
		
		Pageable pageable;
		
		if(key.equals("all")) {
			if(orderby.equals("regdate")) {
				pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("boardhit").descending());
			}
			list = boarddao_jpa.findByBcategory("동행", "", pageable);
		}
		else {
			if(orderby.equals("regdate")) {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("regdate").descending());
			}
			else {
			    pageable = PageRequest.of(page-1, pageSIZE, Sort.by("boardhit").descending());
			}
			list = boarddao_jpa.findByBcategory("동행", keyword, pageable);

		}
		
	    List<List<Board>> rows = new ArrayList<>();
	    List<Board> boardlist = new ArrayList<Board>();
	    List<Board> currentRow = null;
		session.setAttribute("keyword", key);
		session.setAttribute("orderby", orderby);
		
	    for (Board board : list.getContent()) {
	    	
	    	boardlist.add(board);
	        if (currentRow == null || currentRow.size() >= 4) {
	            currentRow = new ArrayList<>();
	            rows.add(currentRow);
	        }
	        currentRow.add(board);
	    }
	    
		mav.addObject("list", boardlist);
		mav.addObject("currentPage", page);
		mav.addObject("totalPages", list.getTotalPages());

		return mav;
	}
    //댓글 수정하기
    @GetMapping("/togetherCommentUpdate")
    @ResponseBody
    public void togetherCommentUpdate(int comno,String comcontent) {
    	commentsdao_jpa.updateByComno(comno, comcontent);
    }
    
    //댓글 삭제하기
    @GetMapping("/togetherCommentDelete")
    @ResponseBody
    public void togetherCommentDelete(@RequestParam int comno) {
    	commentsdao_jpa.deleteById(comno);
    }
    
    //댓글 추가하기
    @PostMapping("/togetherInsertComment")
    public ModelAndView togetherInsertComment(Comments c,int boardno,HttpSession session) {
    	//게시글 번호
    	Board board=new Board();
    	board.setBoardno(boardno);
    	c.setBoard(board);
    	
    	//작성자 
        String id = ((Member)session.getAttribute("m")).getId();
        Member member=new Member();
        member.setId(id);
        c.setMember(member);
        
        //작성일
        c.setRegdate(new Date());
    	
        //댓글 번호
        c.setComno(commentsdao_jpa.findByNext());
        
        //댓글 저장
        commentsdao_jpa.save(c);
        
    	ModelAndView mav=new ModelAndView("redirect:/boards/board/togetherDetail?boardno="+boardno);
    	
    	
    	return mav;
    }
	
}
