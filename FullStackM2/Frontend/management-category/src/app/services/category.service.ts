import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { Category } from '../models/category.model';
import { tap } from 'rxjs/operators';

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

  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.apiUrl, category).pipe(
      tap(() => this.refreshCategories())
    );
  }

  updateCategory(id: number, category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${id}`, category).pipe(
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
    return this.http.put<string>(`${this.apiUrl}/${parentId}/associate/${childId}`, {}).pipe(
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
