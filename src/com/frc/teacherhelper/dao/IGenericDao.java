package com.frc.teacherhelper.dao;

import java.io.Serializable; 
import java.util.Collection; 
import java.util.Iterator; 
import java.util.List; 
import org.hibernate.Criteria; 
import org.hibernate.LockMode; 
import org.hibernate.criterion.DetachedCriteria; 
import org.springframework.orm.hibernate4.HibernateTemplate;


public interface IGenericDao<T extends Serializable, PK extends Serializable> { 
   
  
    // ����������ȡʵ�塣���û����Ӧ��ʵ�壬�׳��쳣�� 
    public T load(PK id); 


    // loadAllWithLock() ? 

    // ����ʵ�� 
    public void update(T entity); 

    // �洢ʵ�嵽���ݿ� 
    public void save(T entity); 

   
    // ɾ��ָ����ʵ�� 
    public void delete(T entity); 


    // ��������ɾ��ָ��ʵ�� 
    public void deleteByKey(PK id); 

    // -------------------- HSQL ---------------------------------------------- 

    // ʹ��HSQL���������� 
    public List find(String queryString); 

 

} 
