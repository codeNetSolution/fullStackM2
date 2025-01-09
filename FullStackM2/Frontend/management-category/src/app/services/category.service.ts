import { Injectable } from '@angular/core';
import { HttpClient, HttpParams  } from '@angular/common/http';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { Category } from '../models/category.model';
import { tap, catchError, map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = 'http://localhost:8080/app/v1/category';

  private categoriesSubject = new BehaviorSubject<Category[]>([]);
  categories$ = this.categoriesSubject.asObservable();

  constructor(private http: HttpClient) {}

  getAllCategories(page: number, size: number): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}?page=${page}&size=${size}`).pipe(
      tap((categories) => this.categoriesSubject.next(categories))
    );
  }

  getFilteredCategories(page: number, size: number, name?: string, creationDate?: string, isRoot?: boolean): Observable<Category[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
  
    if (name) {
      params = params.append('name', name);
    }
    if (creationDate) {
      params = params.append('creationDate', creationDate);
    }

    if(isRoot !== undefined && isRoot !== null) {
      params = params.append('isRoot', isRoot)
    }
  
    return this.http.get<any>(`${this.apiUrl}/filtered`, { params }).pipe(
      tap(data => {
        console.log('Données brutes reçues :', data);
      }),
      map(data => {
        return (data.content || []).map((category: any) => {
          return {
            ...category,
            creationDate: new Date(category.creationDate), 
          };
        });
      }),
      catchError(error => {
        console.error('Error fetching filtered categories:', error);
        return of([]); 
      })
    );
    
  }
  
  



  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.apiUrl, category, {
      headers: { 'Content-Type': 'application/json' }
    }).pipe(
      tap(() => this.refreshCategories())
    );
  }

  updateCategory(id: number, category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${id}`, category, {
      headers: { 'Content-Type': 'application/json' }
    }).pipe(
      tap(() => this.refreshCategories())
    );
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => this.refreshCategories())
    );
  }

  getCategoryDetails(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${id}`);
  }

  associateParentWithChild(parentId: number, childId: number): Observable<string> {
  return this.http.put<string>(`${this.apiUrl}/${parentId}/associate/${childId}`, {}, {
    headers: { 'Content-Type': 'application/json' }
  }).pipe(
    tap(() => this.refreshCategories())
  );
}

  private refreshCategories(): void {
    this.getAllCategories(0, 10).subscribe((categories) => {
      console.log('Categories refreshed:', categories);  
      this.categoriesSubject.next(categories);
    });
  }
}
