import { Component, OnInit } from '@angular/core';
import { Category } from '../../models/category.model';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-category-list',
  standalone: true,
  templateUrl: './category-list.component.html',
  imports: [RouterModule, CommonModule],
  styleUrl: './category-list.component.css'
})
export class CategoryListComponent implements OnInit {

  categories: Category[] = [];
  page = 1;
  size = 10;

  constructor(private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories(): void {
    this.categoryService.getAllCategories(this.page - 1, this.size).subscribe(
      (data) => this.categories = data,
      (error) => console.error('Erreur lors de la récupération des catégories', error)

    );
  }

  deleteCategory(id: number): void {
    if(confirm('Etes-vous sur de vouloir supprimer cette catégorie ? ')) {
      this.categoryService.deleteCategory(id).subscribe(() => {
        this.getCategories();
      });
    }
  }

}
