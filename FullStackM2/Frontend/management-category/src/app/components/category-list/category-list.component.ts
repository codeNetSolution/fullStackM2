// src/app/components/category-list/category-list.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';

@Component({
  selector: 'app-category-list',
  standalone: true,
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class CategoryListComponent implements OnInit {
  categories: Category[] = [];
  selectedParentId?: number; // Ajout de la propriété selectedParentId
  selectedChildId?: number; // Ajout de la propriété selectedChildId

  page: number = 1;  // Page par défaut
  size: number = 10;

  constructor(
    private categoryService: CategoryService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories(): void {
    this.categoryService.getAllCategories(this.page - 1, this.size).subscribe(
      (data) => (this.categories = data),
      (error) => console.error('Erreur lors de la récupération des catégories', error)
    );
  }

  deleteCategory(id?: number): void {
    if (id !== undefined) {
      this.categoryService.deleteCategory(id).subscribe(() => {
        this.getCategories(); // Recharge les catégories après la suppression
      });
    } else {
      console.error("L'ID de la catégorie est indéfini.");
    }
  }

  associateParentWithChild(): void {
    if (this.selectedParentId && this.selectedChildId) {
      this.categoryService.associateParentWithChild(this.selectedParentId, this.selectedChildId).subscribe({
        next: () => {
          console.log('Association réussie');
          this.getCategories(); // Recharge les catégories après l'association
        },
        error: (error) => console.error("Erreur lors de l'association", error)
      });
    } else {
      console.error('Veuillez sélectionner un parent et un enfant');
    }
  }

  navigateToCreate(): void {
    this.router.navigate(['/category/new']);
  }
}
