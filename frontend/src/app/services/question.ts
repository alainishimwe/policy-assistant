import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface QuestionReponse {
  answer: string;
}

@Injectable({
  providedIn: 'root',
})
export class Question {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/questions';

  askQuestion(question: string): Observable<QuestionReponse> {
    return this.http.post<QuestionReponse>(this.apiUrl, {
      question
    });
  }

}
