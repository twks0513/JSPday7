package com.board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import DBPKG.DBConnect;

public class BoardDAO {
	Connection con;
	PreparedStatement ps;
	ResultSet rs;
	public BoardDAO() {
		try {
			con = DBConnect.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<BoardDTO> list() {
		String sql = "select * from test_board order by idgroup desc,step asc";
		ArrayList<BoardDTO> li = new ArrayList<BoardDTO>();
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				getBoard();
				li.add(getBoard());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return li;
	}
	
	private BoardDTO getBoard(){
		BoardDTO dto = new BoardDTO();
		
		try {
			dto.setId(rs.getInt("id"));
			dto.setName(rs.getString("name"));
			dto.setTitle(rs.getString("title"));
			dto.setContent(rs.getString("content"));
			dto.setSavedate(rs.getTimestamp("savedate"));
			dto.setHit(rs.getInt("hit"));
			dto.setIdgroup(rs.getInt("idgroup"));
			dto.setStep(rs.getInt("step"));
			dto.setIndent(rs.getInt("indent"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
		
	}
	
	public void insert(String name,String title,String content) {
		String sql = "insert into test_board(id,name,title,content,idgroup,step,indent) values(test_board_seq.nextval,?,?,?,test_board_seq.currval,0,0)";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, title);
			ps.setString(3, content);			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	  private void upHit(String id) {
	      String sql="update test_board set hit=hit+1 where id="+id;
	      try {
	         ps = con.prepareStatement(sql);
	         ps.executeUpdate();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }
	
	public BoardDTO getContent(String id) {
		upHit(id);
		String sql = "select * from test_board where id="+id;	
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()) {
				return getBoard();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void update(BoardDTO dto) {
		String sql="update test_board set name=?,title=?,content=? where id=?";
		try {
			ps = con.prepareStatement(sql);
			
			ps.setString(1, dto.getName());
			ps.setString(2, dto.getTitle());
			ps.setString(3, dto.getContent());
			ps.setInt(4, dto.getId() );
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delete(String id) {
		String sql = "delete from test_board where id="+id;
		try {
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void replyShap(BoardDTO dto) {
		String sql ="update test_board set step=step+1 where idgroup=? and step > ?";
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, dto.getIdgroup());
			ps.setInt(2, dto.getStep());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reply(BoardDTO dto ) {
		replyShap(dto);
		String sql = "insert into test_board(id,name,title,content,idgroup,step,indent) values(test_board_seq.nextval,?,?,?,?,?,?)";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, dto.getName());
			ps.setString(2, dto.getTitle());
			ps.setString(3, dto.getContent());
			
			ps.setInt(4, dto.getIdgroup());
			ps.setInt(5, dto.getStep()+1);
			ps.setInt(6, dto.getIndent()+1);
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
