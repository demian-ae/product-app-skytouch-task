import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { ProductService } from './product.service';
import { Product } from './product';
import { of, throwError } from 'rxjs';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let productServiceSpy: jasmine.SpyObj<ProductService>;

  const mockProducts: Product[] = [
    { id: 1, name: 'Product A', description: 'Test product A', price: 100, expirationDate: new Date() },
    { id: 2, name: 'Product B', description: 'Test product B', price: 200, expirationDate: new Date() }
  ];

  beforeEach(async () => {
    productServiceSpy = jasmine.createSpyObj('ProductService', ['getProducts', 'addProduct', 'editProduct', 'deleteProduct']);

    await TestBed.configureTestingModule({
      imports: [AppComponent],
      providers: [
        { provide: ProductService, useValue: productServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should call getProducts on init', () => {
    productServiceSpy.getProducts.and.returnValue(of(mockProducts));

    component.ngOnInit();

    expect(productServiceSpy.getProducts).toHaveBeenCalled();
    expect(component.products.length).toBe(2);
  });

  it('should call addProduct and reload products', () => {
    const newProduct = { name: 'New', description: 'Test', price: 50, expirationDate: new Date() };
    const createdProduct: Product = { ...newProduct, id: 3 };

    productServiceSpy.addProduct.and.returnValue(of(createdProduct));
    productServiceSpy.getProducts.and.returnValue(of([...mockProducts, createdProduct]));

    component.addProduct(newProduct);

    expect(productServiceSpy.addProduct).toHaveBeenCalledWith(newProduct);
    expect(productServiceSpy.getProducts).toHaveBeenCalled();
    expect(component.products.length).toBe(3);
  });

  it('should delete product and reload products', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    productServiceSpy.deleteProduct.and.returnValue(of(void 0));
    productServiceSpy.getProducts.and.returnValue(of([mockProducts[1]]));

    component.products = mockProducts;
    component.deleteProduct(1);

    expect(productServiceSpy.deleteProduct).toHaveBeenCalledWith(1);
    expect(productServiceSpy.getProducts).toHaveBeenCalled();
    expect(component.products.find(p => p.id === 1)).toBeUndefined();
  });

  it('should set and clean currentProduct', () => {
    const product = mockProducts[0];
    component.setCurrentProduct(product);
    expect(component.currentProduct).toEqual(product);

    component.cleanCurrentProduct();
    expect(component.currentProduct).toBeNull();
  });

  it('should handle error when addProduct fails', () => {
    const product = { name: 'Fail', description: '', price: 0, expirationDate: new Date() };
    productServiceSpy.addProduct.and.returnValue(throwError(() => new Error('Add error')));

    spyOn(console, 'error');
    component.addProduct(product);

    expect(console.error).toHaveBeenCalledWith('Error adding product:', jasmine.any(String));
  });
});
