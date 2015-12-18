/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 17, 2015
 */

package datastoring;

import datafetching.Fetcher;

public interface DataStorer {
    
    public void notifyDataWasFetched(Fetcher f);
    
}
