import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'; 

@Component({
  selector: 'app-category-list',
  standalone: true,
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css'],
  imports: [CommonModule, FormsModule, RouterModule] 
})
export class CategoryListComponent implements OnInit {
  categories: Category[] = [];
  selectedParentId?: number;
  selectedChildId?: number;
  page: number = 1;
  size: number = 10;
  nameFilter: string = '';
  creationDateFilter: string = '';
  isRootFilter?: boolean;
  noResultsFound: boolean = false;
  isModalOpen: boolean = false;

  constructor(private categoryService: CategoryService, private router: Router) {}

  ngOnInit(): void {
    this.categoryService.categories$.subscribe((data) => {
      this.categories = data;
    });

    this.loadFilteredCategories();
    this.categoryService.getAllCategories(0, 10).subscribe();
  }

  loadFilteredCategories(): void {
    this.categoryService
      .getFilteredCategories(0, 10, this.nameFilter, this.creationDateFilter, this.isRootFilter)
      .subscribe(
        (categories) => {
          this.categories = categories;
          console.log("categoris", categories)
          this.noResultsFound = categories.length === 0;
        },
        (error) => {
          console.error('Failed to load categories:', error);
          this.categories = [];
          this.noResultsFound = true;
        }
      );
  }

  deleteCategory(id?: number): void {
    if (id !== undefined) {
      this.categoryService.deleteCategory(id).subscribe();
    }
  }

  associateParentWithChild(): void {
    if (this.selectedParentId && this.selectedChildId) {
      this.categoryService.associateParentWithChild(this.selectedParentId, this.selectedChildId).subscribe();
    }
  }

  navigateToCreate(): void {
    this.router.navigate(['/category/new']);
  }

  openModal(): void {
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  onAssociate(): void {
    if (this.selectedParentId && this.selectedChildId) {
      this.categoryService
        .associateParentWithChild(this.selectedParentId, this.selectedChildId)
        .subscribe(() => {
          this.closeModal();
        });
    }
  }
}
