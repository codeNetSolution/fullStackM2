import { Component, OnInit ,Input} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CategoryService } from '../../services/category.service';
import { Category } from '../../models/category.model';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';

@Component({
  selector: 'app-category-details',
  standalone: true,
  templateUrl: './category-details.component.html',
  imports: [CommonModule],
  styleUrls: ['./category-details.component.css']
})
export class CategoryDetailsComponent implements OnInit {
  @Input() categoryId?: number;
  category?: Category;
  parentCategory?: Category;
  isRootCategory: boolean = false;

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private location: Location
  ) {}

  ngOnInit(): void {
    if (this.categoryId) {
      this.loadCategoryDetails(this.categoryId);
    }
  }

  private loadCategoryDetails(id: number): void {
    this.categoryService.getCategoryDetails(id).subscribe(
      (category) => {
        this.category = category;
        this.isRootCategory = category.root;
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
  goBack(): void {
    this.location.back(); 
  }
}
