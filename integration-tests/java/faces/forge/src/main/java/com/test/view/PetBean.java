// Metawidget
//
// This file is dual licensed under both the LGPL
// (http://www.gnu.org/licenses/lgpl-2.1.html) and the EPL
// (http://www.eclipse.org/org/documents/epl-v10.php). As a
// recipient of Metawidget, you may choose to receive it under either
// the LGPL or the EPL.
//
// Commercial licenses are also available. See http://metawidget.org
// for details.

package com.test.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.metawidget.inspector.annotation.UiAction;

import com.test.model.Person;
import com.test.model.Pet;

/**
 * Backing bean for Address entities.
 * <p>
 * This class provides CRUD functionality for all Address entities. It focuses purely on Java EE 6
 * standards (e.g. <tt>&#64;ConversationScoped</tt> for state management,
 * <tt>PersistenceContext</tt> for persistence, <tt>CriteriaBuilder</tt> for searches) rather than
 * introducing a CRUD framework or custom base class.
 * 
 * @author <a href="http://kennardconsulting.com">Richard Kennard</a>
 */

@Named
@Stateful
@ConversationScoped
public class PetBean
	implements Serializable {

	private static final long	serialVersionUID	= 1L;

	/*
	 * Support creating and retrieving Address entities
	 */

	private Long				mId;

	public Long getId() {

		return mId;
	}

	public void setId( Long id ) {

		mId = id;
	}

	private Pet	mPet;

	public Pet getPet() {

		return mPet;
	}

	@Inject
	private Conversation				mConversation;

	@PersistenceContext( type = PersistenceContextType.EXTENDED )
	/* package private */EntityManager	mEntityManager;

	@UiAction
	public String create() {

		mConversation.begin();
		return "create?faces-redirect=true";
	}

	public String view() {

		ListDataModel<Pet> pageItems = getPageItems();
		@SuppressWarnings( "unchecked" )
		Pet selectedPet = ( (List<Pet>) pageItems.getWrappedData() ).get( pageItems.getRowIndex() );
		return "view?id=" + selectedPet.getId() + "&faces-redirect=true";
	}

	public void retrieve() {

		if ( FacesContext.getCurrentInstance().isPostback() ) {
			return;
		}

		if ( mConversation.isTransient() ) {
			mConversation.begin();
		}

		if ( mId == null ) {
			mPet = mSearch;
		} else {
			mPet = mEntityManager.find( Pet.class, getId() );
		}
	}

	/*
	 * Support updating and deleting Address entities
	 */

	public String update() {

		mConversation.end();

		try {
			if ( mId == null ) {
				mEntityManager.persist( mPet );
				return "search?faces-redirect=true";
			}
			mEntityManager.merge( mPet );
			return "view?faces-redirect=true&id=" + mPet.getId();
		} catch ( Exception e ) {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return null;
		}
	}

	public String delete() {

		mConversation.end();

		try {
			mEntityManager.remove( mEntityManager.find( Pet.class, getId() ) );
			mEntityManager.flush();
			return "search?faces-redirect=true";
		} catch ( Exception e ) {
			FacesContext.getCurrentInstance().addMessage( null, new FacesMessage( e.getMessage() ) );
			return null;
		}
	}

	/*
	 * Support searching Address entities with pagination
	 */

	private int		mPage;

	private long	mCount;

	private Pet		mSearch	= new Pet();

	public int getPage() {

		return mPage;
	}

	public void setPage( int page ) {

		mPage = page;
	}

	public int getPageSize() {

		return 10;
	}

	public Pet getSearch() {

		return mSearch;
	}

	public void setSearch( Pet search ) {

		mSearch = search;
	}

	@UiAction
	public void search() {

		mPage = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();

		// Populate mCount

		CriteriaQuery<Long> countCriteria = builder.createQuery( Long.class );
		Root<Pet> root = countCriteria.from( Pet.class );
		countCriteria = countCriteria.select( builder.count( root ) ).where( getSearchPredicates( root ) );
		mCount = mEntityManager.createQuery( countCriteria ).getSingleResult();

		// Populate pageItems

		CriteriaQuery<Pet> criteria = builder.createQuery( Pet.class );
		root = criteria.from( Pet.class );
		TypedQuery<Pet> query = mEntityManager.createQuery( criteria.select( root ).where( getSearchPredicates( root ) ) );
		query.setFirstResult( mPage * getPageSize() ).setMaxResults( getPageSize() );

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
		sessionMap.put( Pet.class.getName(), new ListDataModel<Pet>( query.getResultList() ) );
	}

	private Predicate[] getSearchPredicates( Root<Pet> root ) {

		CriteriaBuilder builder = mEntityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = mSearch.getName();
		if ( name != null && !"".equals( name ) ) {
			predicatesList.add( builder.like( root.<String> get( "name" ), '%' + name + '%' ) );
		}
		Person owner = mSearch.getOwner();
		if ( owner != null ) {
			predicatesList.add( builder.equal( root.get( "owner" ), owner ) );
		}
		boolean deceased = mSearch.isDeceased();
		if ( deceased ) {
			predicatesList.add( builder.equal( root.<Boolean> get( "deceased" ), deceased ) );
		}

		return predicatesList.toArray( new Predicate[predicatesList.size()] );
	}

	@SuppressWarnings( "unchecked" )
	public ListDataModel<Pet> getPageItems() {

		// getPageItems must be stored 'above' request level. See
		// http://stackoverflow.com/questions/2118656/hcommandlink-hcommandbutton-is-not-being-invoked

		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
		return (ListDataModel<Pet>) sessionMap.get( Pet.class.getName() );
	}

	public long getCount() {

		return mCount;
	}
}