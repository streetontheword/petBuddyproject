import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { Thread, ThreadSlice } from "../../model";
import { Observable } from "rxjs";

const INIT: ThreadSlice = {
    threads: []
}



@Injectable()
export class ForumStore extends ComponentStore<ThreadSlice>{

    constructor() { super(INIT) }


    // readonly addToStore = this.updater<Thread>(
    //     (slice: ThreadSlice, value: Thread) => {
    //         const newSlice: ThreadSlice = {
    //             threads: []
    //         }
    //         for (let i of slice.threads)
    //             newSlice.threads.push(i)
    //         newSlice.threads.push(value)
    //         return newSlice
    //     }
    // )

    readonly getAllThreads = this.select<Thread[]>(
        (slice: ThreadSlice) => slice.threads

    )

    readonly getMatchingThreadsByTitle = (search: string): Observable<Thread[]> =>
    this.select(state =>
      state.threads.filter(thread =>
        thread.title.toLowerCase().includes(search.toLowerCase())
      )
    );

    readonly loadToStore = this.updater<Thread[]>(
        (_slice: ThreadSlice, values: Thread[]) => {
            return {
                threads: values
            } as ThreadSlice
        }
    )

    getThreadById = (id: string) => 
    this.select((slice: ThreadSlice)=> slice.threads.find(thread => thread._id === id))
        


}


