export interface Filter {
  id: string;
  field: string;
  operation: 'EQ',
  value: string;
}