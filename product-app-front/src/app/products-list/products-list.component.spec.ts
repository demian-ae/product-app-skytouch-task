import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductsListComponent } from './products-list.component';
import { Product } from '../product';
import { By } from '@angular/platform-browser';

describe('ProductsListComponent', () => {
  let component: ProductsListComponent;
  let fixture: ComponentFixture<ProductsListComponent>;

  const mockProducts: Product[] = [
    { id: 1, name: 'Laptop', description: 'Test product A', price: 100, expirationDate: new Date() },
    { id: 2, name: 'Mouse', description: 'Test product B', price: 200, expirationDate: new Date() }
  ];


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductsListComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render product rows', () => {
    component.products = mockProducts;
    fixture.detectChanges();

    const rows = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(rows.length).toBe(2);
    expect(rows[0].textContent).toContain('Laptop');
    expect(rows[1].textContent).toContain('Mouse');
  });

  it('should call reloadProducts when reload button is clicked', () => {
    const reloadSpy = jasmine.createSpy();
    component.reloadProucts = reloadSpy;
    fixture.detectChanges();

    const reloadBtn = fixture.debugElement.query(By.css('.btn-light'));
    reloadBtn.triggerEventHandler('click');
    expect(reloadSpy).toHaveBeenCalled();
  });

  it('should call setCurrentProduct when edit is clicked', () => {
    component.products = mockProducts;
    const setCurrentSpy = jasmine.createSpy();
    component.setCurrentProduct = setCurrentSpy;
    fixture.detectChanges();

    const editBtns = fixture.debugElement.queryAll(By.css('.btn-warning'));
    editBtns[0].triggerEventHandler('click');
    expect(setCurrentSpy).toHaveBeenCalledWith(mockProducts[0]);
  });

  it('should call deleteProduct when delete is clicked', () => {
    component.products = mockProducts;
    const deleteSpy = jasmine.createSpy();
    component.deleteProduct = deleteSpy;
    fixture.detectChanges();

    const deleteBtns = fixture.debugElement.queryAll(By.css('.btn-danger'));
    deleteBtns[1].triggerEventHandler('click');
    expect(deleteSpy).toHaveBeenCalledWith(mockProducts[1].id);
  });

  it('should format date correctly', () => {
    const formatted = component.formatDate('2025-04-01');
    expect(formatted).toBe('01/04/2025');
  });

  it('should return empty string for null date', () => {
    const formatted = component.formatDate(null);
    expect(formatted).toBe('');
  });
});
