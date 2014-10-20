package tk.icudi.durandal.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;

public class DurandalWebServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String command = req.getParameter("command");
		String user = req.getParameter("user");
		
		if("add".equals(command)){
			
			String code = req.getParameter("code");
			String value = req.getParameter("value");
			
			String tickString = req.getParameter("tick");		
			int tick = Integer.valueOf(tickString);
			
			Message msg = new Message();
			msg.setUser(user);
			msg.setCode(code);
			msg.setValue(new Blob(value.getBytes()));
			msg.setTick(tick);
			msg.setDate(new Date());	
			write(msg);

			resp.setContentType("text/plain");
			resp.getWriter().println("done " + new SimpleDateFormat().format(new Date()));
			
		} else if("fetch".equals(command)){
			
			List<Message> result = read();
			
			resp.setContentType("text/plain");
			
			for(Message message : result){
				long difference = System.currentTimeMillis() - message.getDate().getTime();
				String rawvalueString = new String(message.getValue().getBytes());
				String valueString = URLEncoder.encode(rawvalueString, "UTF-8");
//				System.out.println(" --- valueString: " + valueString);
				resp.getWriter().println(message.getKey().getId() + "\t" + message.getTick() + "\t" + message.getUser() + "\t" + message.getCode() + "\t" + new SimpleDateFormat().format(message.getDate()) + "\t" + difference + "\t" + valueString);
			}	
		} else if("delete".equals(command)){
			deleteAll();
		}
	}

	
	private void write(Message msg) {
		EntityManager em = EMF.getEMF().createEntityManager();
		
		if(msg.getCode().equals("build_tower")){
			System.out.println(" --- wrote tick: " + msg.getTick());
		}
		
		try {
			EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(msg);
			tx.commit();
			
		} catch(RuntimeException e){
			System.out.println(" --- Fehler beim Schreiben von " + msg);
			throw e;
		} finally {
			em.close();
		}
	}
	
	private void deleteAll() {
		
//		EntityManager em = EMF.getEMF().createEntityManager();
		PersistenceManager pm = EMF.getPMF().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		
		try{
			
//			CriteriaBuilder builder = em.getCriteriaBuilder();
//			CriteriaQuery<Message> query = builder.createQuery(Message.class);
//			query.from(Message.class);
//			TypedQuery<Message> q = em.createQuery(query); 
//			List<Message> toDelete = q.getResultList();
			

		    Extent<Message> extend = pm.getExtent(Message.class, true);
		    Iterator<Message> iter = extend.iterator();
		    while (iter.hasNext()) {
				tx.begin();
		        pm.deletePersistent(iter.next());
			    tx.commit();
		    }

		    
		}finally{
			pm.close();
//			em.close();
		}
		
		System.out.println(" --- Datastore deleted");

	}
	
	private List<Message> read() {
		EntityManager em = EMF.getEMF().createEntityManager();
		
		try {
			
			CriteriaBuilder builder = em.getCriteriaBuilder();
			CriteriaQuery<Message> query = builder.createQuery(Message.class);
			Root<Message> root = query.from(Message.class);
			Order order = builder.desc(root.get("date"));
			query.orderBy(order);
			
			TypedQuery<Message> q = em.createQuery(query); 
			return q.getResultList();

		} finally {
			em.close();
		}
	}
}
