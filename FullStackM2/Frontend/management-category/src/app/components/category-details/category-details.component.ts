import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category-details',
  standalone: true,
  templateUrl: './category-details.component.html',
  imports: [CommonModule],
  styleUrls: ['./category-details.component.css']
})
export class CategoryDetailsComponent implements OnInit {
  category?: Category;
  parentCategory?: Category;

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.categoryService.getCategoryDetails(id).subscribe(
      (category) => {
        this.category = category;
        if (category.parentId) {
          this.fetchParentCategory(category.parentId);
        }
      },
      (error) => console.error('Error fetching category details', error)
    );
  }

  fetchParentCategory(parentId: number): void {
    this.categoryService.getCategoryDetails(parentId).subscribe(
      (parentCategory) => this.parentCategory = parentCategory,
      (error) => console.error('Error fetching parent category', error)
    );
  }
}
