import { Component, OnInit , LOCALE_ID } from '@angular/core';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'; 
import { CategoryDetailsComponent } from '../category-details/category-details.component';
import { CategoryFormComponent } from '../category-form/category-form.component';


@Component({
  selector: 'app-category-list',
  standalone: true,
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css'],
  imports: [CommonModule, FormsModule, RouterModule,CategoryDetailsComponent,CategoryFormComponent]
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
  selectedCategory?: Category;
  selectedCategoryId?: number;
  isEditMode: boolean = false;
  isDetailsModalOpen: boolean = false; 
  isEditModalOpen: boolean = false;
  afterDateFilter: string = '';
  beforeDateFilter: string = '';
  filteredChildren: Category[] = [];



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
      .getFilteredCategories(0, 10, this.nameFilter, this.afterDateFilter, this.beforeDateFilter, this.creationDateFilter,this.isRootFilter)
      .subscribe(
        (categories) => {
          this.categories = categories;
          this.updateFilteredChildren();
          this.noResultsFound = categories.length === 0;
        },
        (error) => {
          console.error('Failed to load categories:', error);
          this.categories = [];
          this.noResultsFound = true;
        }
      );
  }

  updateFilteredChildren(): void {
    if (this.selectedParentId) {
        this.filteredChildren = this.categories.filter((category) => {
            const exclude = Number(category.id) === Number(this.selectedParentId);
            return !exclude;
        });
    } else {
        this.filteredChildren = [...this.categories];
    }
}

  onParentChange(): void {
    this.updateFilteredChildren();
  }
  
  resetFilters(): void {
    this.nameFilter = '';
    this.creationDateFilter = '';
    this.afterDateFilter = '';
    this.beforeDateFilter = '';
    this.isRootFilter = undefined;
    this.loadFilteredCategories();
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

  openDetailsModal(categoryId: number): void {
    this.selectedCategoryId = categoryId;
    this.isDetailsModalOpen = true;
  }

  openEditModal(categoryId: number): void {
    this.selectedCategoryId = categoryId;
    this.isEditModalOpen = true;
  }

  closeDetailsModal(): void {
    this.isDetailsModalOpen = false;
    this.selectedCategoryId = undefined;
  }
  
  closeEditModal(): void {
    this.isEditModalOpen = false;
    this.selectedCategoryId = undefined;
  }

  openCreateModal(): void {
    this.selectedCategoryId = undefined;
    this.isEditModalOpen = true; 
  }
  

  
}
